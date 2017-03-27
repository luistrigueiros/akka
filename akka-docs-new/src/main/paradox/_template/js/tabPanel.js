$(function() {
  $('.docTabPanel .tabPanelList li').on('click', function() {
    var $panel = $(this).closest('.docTabPanel');
      $panel.find('.tabPanelList li.active').removeClass('active');
        $(this).addClass('active');
      //figure out which panel to show
      var panelToShow = $(this).attr('rel');
      //hide current panel
      $panel.find('.tabPanel.active').hide(0, showNextPanel);
        //show next panel
        function showNextPanel() {
          $(this).removeClass('active');
          $('#'+panelToShow).show(0, function() {
          $(this).addClass('active');
        });
      }
  });
});