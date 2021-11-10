$(document).ready(function (){
    $("#users_list").each(function (_, table){
        $(table).DataTable({
            responsive: true,
            select: true,
        });
    });

    $("#meetings_list").each(function (_, table){
        $(table).DataTable({
            responsive: true,
            select: true,
            "order": [],
            "bSort" : false,
        });
    });

});