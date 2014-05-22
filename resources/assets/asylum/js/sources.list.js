$(document).ready(
    function () {
        avails = {
            "lucid": ["zorp", "syslog-ng-3.3", "syslog-ng-3.4", "syslog-ng-3.5",
                     "syslog-ng", "syslog-ng-devel"],
            "precise": ["zorp", "syslog-ng-3.3", "syslog-ng-3.4", "syslog-ng-3.5",
                       "syslog-ng", "syslog-ng-devel"],
            "saucy": ["syslog-ng-3.3", "syslog-ng-3.4", "syslog-ng-3.5",
                     "syslog-ng", "syslog-ng-devel"],
            "trusty": ["syslog-ng-3.4", "syslog-ng-3.5",
                      "syslog-ng", "syslog-ng-devel"],
            "utopic": ["syslog-ng-3.4", "syslog-ng-3.5",
                      "syslog-ng", "syslog-ng-devel"],

            "squeeze": ["zorp", "syslog-ng-3.3", "syslog-ng-3.4", "syslog-ng-3.5",
                       "syslog-ng", "syslog-ng-devel"],
            "wheezy": ["zorp", "syslog-ng-3.3", "syslog-ng-3.4", "syslog-ng-3.5",
                      "syslog-ng", "syslog-ng-devel"],
            "jessie": ["syslog-ng-3.3", "syslog-ng-3.4", "syslog-ng-3.5",
                      "syslog-ng", "syslog-ng-devel"],
            "unstable": ["syslog-ng-3.3", "syslog-ng-3.4", "syslog-ng-3.5",
                        "syslog-ng", "syslog-ng-devel"]
        };

        all_components = ["zorp", "syslog-ng-3.3", "syslog-ng-3.4", "syslog-ng-3.5"];

        function get_data_from_distrib_form () {
            var distrel = $("#distro-select").val().split("-");
            var components = [];
            var zorp_extra = false;

            var sng = $("#sng-select").val();
            if (sng != "syslog-ng-none") {
                components.push(sng);
            }

            var zorp = $("#zorp-select").val();
            if (zorp != "zorp-none") {
                components.push("zorp");
                zorp_extra = true;
            }

            return {
                "dist": distrel[0],
                "release": distrel[1],
                "components": components,
                "zorp_extra": zorp_extra,
            }
        }

        function redraw_sources_list(data) {
            var new_text = 
                "deb       http://packages.madhouse-project.org/" + data.dist + "   " + data.release + "   " + data.components.join(" ") + "\n" +
                "deb-src   http://packages.madhouse-project.org/" + data.dist + "   " + data.release + "   " + data.components.join(" ") + "\n";
            if (data.zorp_extra) {
                new_text += "\ndeb       http://packages.madhouse-project.org/zorp-kernel   kernel   2.6 3.2";
            } else {
                new_text += "\n\n";
            }
            var box = $("pre code")[1];
            $(box).fadeOut(400, 
                                     function () {
                                         $(box).text(new_text);
                                         $(box).fadeIn();
                                     });
        }

        function validate_selection(data) {
            var alert_components = [];
            var rel_comps = avails[data.release];

            all_components.forEach(function (val) {
                if (data.components.indexOf (val) != -1 &&
                    rel_comps.indexOf (val) == -1) {
                    data.components.splice(data.components.indexOf (val), 1);
                    alert_components.push(val);

                    if (val == "zorp") {
                        data.zorp_extra = false;
                    }
                }
            });

            if (alert_components.length > 0) {
                $("#alert-box").text ("The selected distribution does not have packages for the following components: " + alert_components.join(", ")).fadeIn();
            } else {
                $("#alert-box").fadeOut();
            }
            return data;
        }

        function reset_controls(data) {
            $("#sng-select").find("option")
                .each(function (x) {
                    if (avails[data.release].indexOf (this.value) == -1 &&
                        this.value != "syslog-ng-none") {
                        $(this).attr("disabled", true);
                    } else {
                        $(this).removeAttr("disabled");
                    }
                });

            $("#zorp-select").find("option")
                .each(function (x) {
                    if (avails[data.release].indexOf (this.value) == -1 &&
                        this.value != "zorp-none") {
                        $(this).attr("disabled", true);
                    } else {
                        $(this).removeAttr("disabled");
                    }
                });
        }

        $("#dist-select").change (
            function () {
                var s = get_data_from_distrib_form();
                reset_controls(s);
                redraw_sources_list(validate_selection(s));
            });
    });
