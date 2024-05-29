// $(()=>{
//     axios('http://172.16.82.2:9234/getXml?fileName=FUMCOM')
//         .then((res) => {
//             let thead = $('table').children('thead').children('tr')
//             let tbody = $('table').children('tbody')
            
//             $.each(res.data, (i, n) => {
//                 if (n.isFather != 1){
//                     $('#select-area').append(
//                         '<div class="form-check">'+
//                             '<input class="form-check-input" type="checkbox" value="" checked>'+
//                             '<label class="form-check-label" for="flexCheckDefault">'+n.colName+'</label>'+
//                             '<label class="form-check-label-c" for="flexCheckDefault">'+'&nbsp'+n.head+'</label>'+
//                         '</div>'
//                     )
                
//                     $(thead).append(
//                         '<th scope="col" class="'+n.colName+'">'+n.head+'</th>'
//                     )
//                 }
//             })

//             let rowCount = 0
//             axios('http://172.16.82.2:9234/getData?fileName=FUMCOM&rows=10')
//                 .then((res) => {
//                     $.each(res.data, (recnum, rec) => {
//                         let record = JSON.parse(rec)
//                         console.log(record)

//                         $(tbody).append(
//                             '<tr id="'+(++rowCount)+'FUMCOM'+'">'+
//                                 '<th scope="row">'+ (rowCount) +'</th>'+
//                             '</tr'
//                         )

//                         let cols = $('thead').children('tr').children('th')
//                         $.each(cols, (i, n) => {
//                             let field = $(n).attr('class')
                            
//                             if (i > 0) {
//                                 $('#'+(recnum+1)+"FUMCOM").append(
//                                     '<td class="'+field+'">'+record[field]+'</td>'
//                                 )
//                             }
//                         })
//                     })
//                 })
//                 .catch((err) => console.log(err))

//             $('.form-check-input').on('change', (event) => {
//                 let colName = $(event.target).next().text()
//                 let thead = $('thead').children('tr').children('.'+colName)
//                 let tbody = $('tbody').children().children('.'+colName)
    
//                 if ($(event.target).prop('checked')) {
//                     $(thead).show()
//                     $(tbody).show()
//                 }else {
//                     $(thead).hide()
//                     $(tbody).hide()
//                 }
//             })

//             $('#selectAll').on('click', () => {
//                 $('.form-check-input').prop('checked', true)
//                 $('.form-check-input').change()
//             })

//             $('#selectNo').on('click', () => {
//                 $('.form-check-input').prop('checked', false)
//                 $('.form-check-input').change()
//             })            
//         })
//         .catch((err) => console.log(err))
// })

$('#read').on('click', () => {
    let fileName = '';

    if ($('select').val() != 1)
        window.alert('請選擇檔案!')
    else
        fileName = $('select option:selected').text()

    let url = 'http://172.16.82.2:9234/getXml?fileName=' + fileName

    axios(url)
        .then((res) => {
            $('#select-area').empty()
            $('thead tr').empty()
            $('thead tr').append('<th scope="col">#</th>')
            $('tbody').empty()

            let thead = $('table').children('thead').children('tr')
            let tbody = $('table').children('tbody')
            
            $.each(res.data, (i, n) => {
                if (n.isFather != 1){
                    $('#select-area').append(
                        '<div class="form-check">'+
                            '<input class="form-check-input" type="checkbox" value="" checked>'+
                            '<label class="form-check-label" for="flexCheckDefault">'+n.colName+'</label>'+
                            '<label class="form-check-label-c" for="flexCheckDefault">'+'&nbsp'+n.head+'</label>'+
                        '</div>'
                    )
                
                    $(thead).append(
                        '<th scope="col" class="'+n.colName+'">'+n.head+'</th>'
                    )
                }
            })

            let rowCount = 0
            url = 'http://172.16.82.2:9234/getData?fileName=' + fileName + '&rows=10'
            axios(url + '&rows=10')
                .then((res) => {
                    $.each(res.data, (recnum, rec) => {
                        let record = JSON.parse(rec)
                        console.log(record)

                        $(tbody).append(
                            '<tr id="'+(++rowCount)+'FUMCOM'+'">'+
                                '<th scope="row">'+ (rowCount) +'</th>'+
                            '</tr'
                        )

                        let cols = $('thead').children('tr').children('th')
                        $.each(cols, (i, n) => {
                            let field = $(n).attr('class')
                            
                            if (i > 0) {
                                $('#'+(recnum+1)+"FUMCOM").append(
                                    '<td class="'+field+'">'+record[field]+'</td>'
                                )
                            }
                        })
                    })
                })
                .catch((err) => console.log(err))

            $('.form-check-input').on('change', (event) => {
                let colName = $(event.target).next().text()
                let thead = $('thead').children('tr').children('.'+colName)
                let tbody = $('tbody').children().children('.'+colName)
    
                if ($(event.target).prop('checked')) {
                    $(thead).show()
                    $(tbody).show()
                }else {
                    $(thead).hide()
                    $(tbody).hide()
                }
            })

            $('#selectAll').on('click', () => {
                $('.form-check-input').prop('checked', true)
                $('.form-check-input').change()
            })

            $('#selectNo').on('click', () => {
                $('.form-check-input').prop('checked', false)
                $('.form-check-input').change()
            })            
        })
        .catch((err) => console.log(err))    
})