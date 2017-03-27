$(document).ready(function() {
  $(window).scroll(function () {
    if ($(window).scrollTop() > 150) {
      $('#stickySearch').addClass('stickyFixed');
      $('#stickyFill').addClass('stickyFill');
    }
    if ($(window).scrollTop() < 151) {
      $('#stickySearch').removeClass('stickyFixed');
      $('#stickyFill').removeClass('stickyFill');
    }
  });
});