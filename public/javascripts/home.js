function generate_grid(resultJson) {
    var theTable = "";
    var isSuccess = false;
    var isFail = false;
    var mines = [];
    if(resultJson.status === 'SUCCESS'){
        isSuccess = true;
    }else if(resultJson.status === 'FAILED'){
        isFail = true;
    }
    for(var j = 0;j < resultJson.dimension; j++){
        theTable += '<tr>';
        for(var k = 0; k < resultJson.dimension; k++){
            id = j + '_' + k;
            cell = resultJson.grid.cells[j][k];
            if(isFail){
                if(cell.value === '*')
                    mines.push(id);
            }
            if (cell.hidden)
                theTable += '<td><button id = '+ id +' name = '+ id +' class = "panel_cell"></button></td>';
            else
                theTable += '<td><button id = '+ id +' name = '+ id +' class = "panel_cell visible">'+ cell.value +'</button></td>';                    
            
            
        }
        theTable += '</tr>';
    }
    $('#minesweeper_table').empty();
    $('#minesweeper_table').append(theTable);
    if(isFail){
        for (var i = mines.length - 1; i >= 0; i--) {
            id = mines[i];
            $('#' + id).removeClass('visible');
            $('#' + id).addClass('mine');
        }
        $('#minesweeper_table').find("button").attr("disabled", "disabled"); //Disable table
    }else if(isSuccess){
        $('.winner_alert').fadeIn();
        $('#minesweeper_table').find("button").attr("disabled", "disabled"); //Disable table
        $('#fireworks_container').show();
        setTimeout(function() {
            $('#fireworks_container').hide();
            $('.winner_alert').fadeOut();
        }, 5000);        
        $('#fireworks_container').click(); //Trigger Fireworks
    }
}

function generate_submit_json(resultJson) {
    for(var j = 0;j < resultJson.panel.dimension; j++){
        for(var k = 0; k < resultJson.panel.dimension; k++){
            var id = j + '_' + k;
            var tableCell = document.getElementById(id);
            if (tableCell.classList.contains("visible")) {
                resultJson.panel.grid.cells[j][k].hidden = false;
            }
        }
    }
    return resultJson;
}