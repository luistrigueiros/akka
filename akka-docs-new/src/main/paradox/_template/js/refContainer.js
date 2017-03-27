$(document).ready(function() {
  $(window).scroll(function () {
      console.log($(window).scrollTop())
    if ($(window).scrollTop() > 150) {
      $('#referenceContainer').addClass('refContainerFixed');
    }
    if ($(window).scrollTop() < 151) {
      $('#referenceContainer').removeClass('refContainerFixed');
    }
  });
});