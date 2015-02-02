require(['preview','amplify','communicator'], function () {
    'use strict';

    var origin = 'http://127.0.0.1:8080';
    var Events = crafter.studio.preview.Topics;
    var communicator = new crafter.Communicator({ window: window.parent, origin: origin }, origin);

    // When the page has successfully loaded, notify the host window of it's readiness
    // TODO possibly switchable to DOMContentLoaded. Do we need to wait for assets to load?
    // window.addEventListener('load', function () {
        communicator.publish(Events.GUEST_SITE_LOAD, {
            /*  TODO Does the host require anything to be provided by the guest?  */
            location: window.location.href,
            url: (function (location) {
                return location.href
                    .replace(location.origin, '')
            }) (window.location)
        });
    // }, false);

    function loadCss(url) {
        var link = document.createElement("link");
        link.type = "text/css";
        link.rel = "stylesheet";
        link.href = url;
        document.getElementsByTagName("head")[0].appendChild(link);
    }

    var $overlay = $('<div class="crafter-studio-ice-overlay" style="display: none;"></div>'),
        count = 0;

    function initICETarget(elem) {

        var $elem = $(elem),
            position = $elem.offset(),
            iceRef = $elem.data('studioIce') + '-' + count++;

        $elem.attr('data-studio-ice-target', iceRef);

        $('<i class="crafter-studio-ice-indicator" data-studio-ice-trigger="%@"></i>'.fmt(iceRef)).css({
            top: position.top,
            left: position.left
        }).appendTo('body');

        $elem.on('mouseover', function (e) {

        });

        $overlay.on('mouseleave', function () {

        });

    }

    function initICERegions() {
        $('.crafter-studio-ice-indicator').remove();
        var elems = document.querySelectorAll('[data-studio-ice]');
        for (var i = 0; i < elems.length; ++i) {
            initICETarget(elems[i]);
        }
    }

    $(document).on('mouseover', '[data-studio-ice]', function (e) {

    });

    $(document).on('mouseleave', '[data-studio-ice]', function () {

    });

    $(document).on('mouseout', '.crafter-studio-ice-overlay', function () {

    });

    $(document).on('mouseover', '.crafter-studio-ice-indicator', function (e) {

        var $i = $(this),
            $e = $('[data-studio-ice-target="%@"]'.fmt($i.data('studioIceTrigger'))),
            iceId = $e.data('studioIce');

        var position = $e.offset(),
            props    = {
                top     : position.top,
                left    : position.left,
                width   : $e.width(),
                height  : $e.height()
            };

        $overlay.css(props).fadeIn('fast');
    });

    $(document).on('mouseout', '.crafter-studio-ice-indicator', function (e) {
        $overlay.fadeOut('fast');
    });

    $(document).on('click', '.crafter-studio-ice-indicator', function (e) {

        var $i = $(this),
            $e = $('[data-studio-ice-target="%@"]'.fmt($i.data('studioIceTrigger'))),
            iceId = $e.data('studioIce');

        var position = $e.offset(),
            props    = {
                top     : position.top,
                left    : position.left,
                width   : $e.width(),
                height  : $e.height()
            };

        // $overlay.css(props).show();

        props.iceId      = iceId;
        props.scrollTop  = $(window).scrollTop();
        props.scrollLeft = $(window).scrollLeft();

        communicator.publish(Events.ICE_ZONE_ON, props);

    });

    $(window).resize(function () {
        initICERegions();
    });

    loadCss('/studio/static-assets/styles/studio-guest.css');
    $overlay.appendTo('body');
    initICERegions();

});
