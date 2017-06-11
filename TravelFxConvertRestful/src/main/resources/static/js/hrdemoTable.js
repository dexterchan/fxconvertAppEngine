/**
 * http://usejsdoc.org/
 */
$(document).ready(function() {
    //var table = $('#example').DataTable();
 
	$('#displaypanel').show()
	$('#displaypanel2').hide()
	var table = $('#example').DataTable( {
        "paging":   false,
        "ordering": true,
        "info":     false,
        "columnDefs": [
            { className: "dt-right", "targets": [3,5] },
            { className: "dt-nowrap", "targets": [0,1] }
          ]
    } );
    
    $('#example tbody').on( 'click', 'tr', function () {
        $(this).toggleClass('selected');
    } );
 
    $('#button').click( function () {
        alert( table.rows('.selected').data().length +' row(s) selected' );
    } );
} );