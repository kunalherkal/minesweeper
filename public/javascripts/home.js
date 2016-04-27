function generate_grid(resultJson) {
    var theTable = "";
    for(var j = 0;j < resultJson.dimension; j++){
        theTable += '<tr>';
        for(var k = 0; k < resultJson.dimension; k++){
            id = j + '_' + k;
            cell = resultJson.grid.cells[j][k];
            if (cell.hidden)
                theTable += '<td><button id = '+ id +' name = '+ id +' class = "panel_cell" style="background-color:#ebebe0"></button></td>';
            else
                theTable += '<td><button id = '+ id +' name = '+ id +' class = "panel_cell visible">'+ cell.value +'</button></td>';
        }
        theTable += '</tr>';
    }
    $('#minesweeper_table').empty();
    $('#minesweeper_table').append(theTable);
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