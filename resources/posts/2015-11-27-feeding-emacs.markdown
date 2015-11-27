---
title: "Feeding Emacs"
date: 2015-11-27 15:15
tags: [Hacking, Emacs, .planet.debian]
---

For the past fifteen years, I have been [tweaking][blog:emacs-redone] my
`~/.emacs` [continously][blog:spacemacs], most recently by switching to
[Spacemacs][spacemacs]. With that switch done, I started to migrate a few more
things to Emacs, an Atom/RSS reader being one that's been in the queue for
years - ever since Google Reader shut down. Since March 2013, I have been a
Feedly user, but I wanted to migrate to something better for a long time. I
wanted to use Free Software, for one.

I saw a mention of [Elfeed][elfeed] somewhere a little while ago, and in the
past few days, I decided to give it a go. The results are pretty amazing.

[blog:emacs-redone]: /blog/2015/01/10/emacs-setup-redone/
[blog:spacemacs]: /blog/2015/11/16/spacemacs/
[spacemacs]: https://github.com/syl20bnr/spacemacs
[elfeed]: https://github.com/skeeto/elfeed

<!-- more -->

For now, I'm using [my own fork of Elfeed][elfeed:algernon], because of some new
features I'm adding, which were not submitted upstream yet. It's all possible
using the upstream sources, if one monkey-patches a few functions - but that's
ugly.

[elfeed:algernon]: https://github.com/algernon/elfeed

Anyhow, this is how my feed reader looks right now:

[![Elfeed setup][img:elfeed:thumb]][img:elfeed]

[img:elfeed:thumb]: /assets/asylum/images/posts/feeding-emacs/elfeed.thumb.png
[img:elfeed]: /assets/asylum/images/posts/feeding-emacs/elfeed.png

This required quite a bit of elisp to be written, and depends on
[popwin][popwin] and [powerline][powerline], but I'm very happy with the
results.

[popwin]: http://melpa.org/#/popwin
[powerline]: http://melpa.org/#/powerline

Without much further ado, the magic to make this happen:

<div class="pygmentize" data-language="elisp">(defun feed-reader/stats ()
  "Count the number of entries and feeds being currently displayed."
  (if (and elfeed-search-filter-active elfeed-search-filter-overflowing)
      (list 0 0 0)
    (cl-loop with feeds = (make-hash-table :test 'equal)
             for entry in elfeed-search-entries
             for feed = (elfeed-entry-feed entry)
             for url = (elfeed-feed-url feed)
             count entry into entry-count
             count (elfeed-tagged-p 'unread entry) into unread-count
             do (puthash url t feeds)
             finally
             (cl-return
              (list unread-count entry-count (hash-table-count feeds))))))

(defun feed-reader/search-header ()
  "Returns the string to be used as the Elfeed header."
  (let* ((separator-left (intern (format "powerline-%s-%s"
                                         (powerline-current-separator)
                                         (car powerline-default-separator-dir))))
         (separator-right (intern (format "powerline-%s-%s"
                                          (powerline-current-separator)
                                          (cdr powerline-default-separator-dir)))))
    (if (zerop (elfeed-db-last-update))
        (elfeed-search--intro-header)
      (let* ((db-time (seconds-to-time (elfeed-db-last-update)))
             (update (format-time-string "%Y-%m-%d %H:%M:%S %z" db-time))
             (stats (feed-reader/stats))
             (search-filter (cond
                             (elfeed-search-filter-active
                              "")
                             (elfeed-search-filter
                              elfeed-search-filter)
                             ("")))
             (lhs (list
                   (powerline-raw (concat search-filter " ") 'powerline-active1 'l)
                   (funcall separator-right 'powerline-active1 'mode-line)))
             (center (list
                      (funcall separator-left 'mode-line 'powerline-active2)
                      (destructuring-bind (unread entry-count feed-count) stats
                        (let* ((content (format " %d/%d:%d" unread entry-count feed-count))
                               (help-text nil)
                               )
                          (if url-queue
                              (let* ((total (length url-queue))
                                     (in-process (cl-count-if #'url-queue-buffer url-queue)))
                                (setf content (concat content " (*)"))
                                (setf help-text (format " %d feeds pending, %d in process ... "
                                                        total in-process))))
                          (propertize content
                                      'face 'powerline-active2
                                      'help-echo help-text)))
                      (funcall separator-right 'powerline-active2 'mode-line)))
             (rhs (list
                   (funcall separator-left 'mode-line 'powerline-active1)
                   (powerline-raw (concat " " update) 'powerline-active1 'r))))

        (concat (powerline-render lhs)
                (powerline-fill-center nil (/ (powerline-width center) 2.0))
                (powerline-render center)
                (powerline-fill nil (powerline-width rhs))
                (powerline-render rhs))))))

(defun popwin:elfeed-show-entry (buff)
  (popwin:popup-buffer buff
                       :position 'right
                       :width 0.5
                       :dedicated t
                       :noselect nil
                       :stick t))

(defun popwin:elfeed-kill-buffer ()
  (interactive)
  (let ((window (get-buffer-window (get-buffer "*elfeed-entry*"))))
    (kill-buffer (get-buffer "*elfeed-entry*"))
    (delete-window window)))

(setq elfeed-show-entry-switch #'popwin:elfeed-show-entry
      elfeed-show-entry-delete #'popwin:elfeed-kill-buffer
      elfeed-search-header-function #'feed-reader/search-header)</div>

I probably learned more elisp with this experiment than in the past 15 years
combined, and I finally start to understand how properties and powerline work.
Great! Looking forward to simplify and then push the current setup further!

I'd like to be able to change the search page layout, for example: I want the
tags on a separate column. Elfeed also looks like a great candidate for a number
of pull requests I plan to make in December...
