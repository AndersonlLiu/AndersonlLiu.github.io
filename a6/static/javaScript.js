function getMiles(meters) {
    mile = meters*0.000621371192;
    return mile.toFixed(2);
}

function getMeters(miles) {
    meter = parseInt(miles*1609.344);
    return meter;
}

function infoSubmit () {
    var keyword;
    var distance;
    var category;
    var location;
    var lat;
    var lng;
    var auto_location = document.getElementById('autogeo').checked;
    keyword = document.getElementById('keyword').value;
    category = document.getElementById('category').value;

    if (document.getElementById('distance').value.length == 0){
        distance = getMeters(10);
    } else {
        distance = getMeters(parseFloat(document.getElementById('distance').value));
    }
    //console.log(distance);

    if (category === 'Default'){
        category = 'all';
    }
    //console.log(category);

    if (auto_location) {
        $.ajax({
            type: 'GET',
            url: 'https://ipinfo.io/?token=925c24435c3495',
            success: function (response) {
                location = response.loc;
                lat = location.split(',')[0];
                lng = location.split(',')[1];
                returnTable(lat, lng, keyword, category, distance);
            },
          });
    } else {
        location = document.getElementById('location').value;
        $.ajax({
            type: 'GET',
            url: 'https://maps.googleapis.com/maps/api/geocode/json?address=' + location + '&key=AIzaSyC6MV8jAJBlRHVIonGKi_jEdfMbI2VxXUk',
            success: function (response) {
                lat = response.results[0].geometry.location.lat;
                lng = response.results[0].geometry.location.lng;
                returnTable(lat, lng, keyword, category, distance);
            }
        });
    }
}

function returnTable(lat, lng, keyword, category, distance) {
    var num;
    var business;
    var row;
    var row_data_1;
    var row_data_2;
    var row_data_3;
    var row_data_4;
    var row_data_5;
    var tbody;
    $.ajax({
        type: 'GET',
        url: 'https://static-welder-362708.wl.r.appspot.com/business/' + lat + '/' + lng + '/' + keyword + '/' + category + '/' + distance,
        success: function (response) {
            result = JSON.parse(response);
            console.log(result);
            if (result.total !== 0) {
                document.getElementById("table_area").style.display = "block";
                document.getElementById('table_area').style.backgroundColor= "lightgray";
                //console.log(response);
                table = document.createElement('table');
                table.setAttribute('id', 'table');

                thead = document.createElement('thead');
                thead.setAttribute('id', 'thead');
                tr = document.createElement('tr');
                th1 = document.createElement('th');
                th1.innerHTML = 'No.';
                th2 = document.createElement('th');
                th2.innerHTML = 'Image';
                th3 = document.createElement('th');
                th3.innerHTML = 'Business Name';
                th3.setAttribute('onclick', `sortNameTable(${2})`);
                th4 = document.createElement('th');
                th4.innerHTML = 'Rating';
                th4.setAttribute('onclick', `sortNumTable(${3})`);
                th5 = document.createElement('th');
                th5.innerHTML = 'Distance(miles)';
                th5.setAttribute('onclick', `sortNumTable(${4})`);
                tr.appendChild(th1);
                tr.appendChild(th2);
                tr.appendChild(th3);
                tr.appendChild(th4);
                tr.appendChild(th5);
                thead.append(tr);
                table.append(thead);

                tbody = document.createElement('tbody');
                tbody.setAttribute('id', 'tbody');
                table.append(tbody);
                num = 1;
                for (var i=0; i<result.businesses.length; i++) {
                    business = result.businesses[i]
                    id = business.id;
                    row = document.createElement('tr');
                    row_data_1 = document.createElement('td');
                    row_data_1.setAttribute('class', 'No');
                    row_data_1.innerHTML = num;
                    row_data_2 = document.createElement('td');
                    row_data_2.innerHTML = '<img src=' + business.image_url + ' width="110" height="110">';
                    row_data_3 = document.createElement('td');
                    row_data_3.setAttribute('onclick', `displayDetail('${id}')`);
                    row_data_3.innerHTML = '<td>' + business.name + '</td>';
                    row_data_4 = document.createElement('td');
                    row_data_4.innerHTML = business.rating;
                    row_data_5 = document.createElement('td');
                    row_data_5.innerHTML = getMiles(business.distance);
                    num++;

                    row.appendChild(row_data_1);
                    row.appendChild(row_data_2);
                    row.appendChild(row_data_3);
                    row.appendChild(row_data_4);
                    row.appendChild(row_data_5);
                
                    tbody.append(row);
                }
                document.getElementById('table_area').append(table);
            
            } else {
                document.getElementById("table_area").style.display = "block";
                document.getElementById('table_area').style.backgroundColor= "white";
                p = document.createElement('h2');
                p.setAttribute('id', 'noResult');
                p.innerHTML = 'No record has been found';
                document.getElementById('table_area').append(p);
            }


            
        }
    });
}
//resource: https://www.w3schools.com/howto/howto_js_sort_table.asp
function sortNameTable(n) {
    var table, rows, switching, i, x, y, shouldSwitch, dir, switchcount = 0;
    var no = 1;
    table = document.getElementById("table");
    switching = true;
    dir = "asc";
    while (switching) {
      switching = false;
      rows = table.rows;
      for (i = 1; i < (rows.length - 1); i++) {
        shouldSwitch = false;
        x = rows[i].getElementsByTagName("TD")[n];
        y = rows[i + 1].getElementsByTagName("TD")[n];
        if (dir == "asc") {
          if (x.innerHTML.toLowerCase() > y.innerHTML.toLowerCase()) {
            shouldSwitch = true;
            break;
          }
        } else if (dir == "desc") {
          if (x.innerHTML.toLowerCase() < y.innerHTML.toLowerCase()) {
            shouldSwitch = true;
            break;
          }
        }
      }
      if (shouldSwitch) {
        rows[i].parentNode.insertBefore(rows[i + 1], rows[i]);
        switching = true;
        switchcount ++;
      } else {
        if (switchcount == 0 && dir == "asc") {
          dir = "desc";
          switching = true;
        }
      }
    }
    let items = document.getElementsByClassName("No");
    for (var i = 0; i < items.length; i++) {
        items[i].innerHTML = no;
        no++;
    }
  }
//resource: https://www.w3schools.com/howto/howto_js_sort_table.asp
function sortNumTable(n) {
    var table, rows, switching, i, x, y, shouldSwitch, dir, switchcount = 0;
    var no = 1;
    table = document.getElementById("table");
    switching = true;
    dir = "asc";
    while (switching) {
      switching = false;
      rows = table.rows;
      for (i = 1; i < (rows.length - 1); i++) {
        shouldSwitch = false;
        x = rows[i].getElementsByTagName("TD")[n];
        y = rows[i + 1].getElementsByTagName("TD")[n];
        if (dir == "asc") {
            if (Number(x.innerHTML) > Number(y.innerHTML)) {
                shouldSwitch = true;
            break;
            } 
        }else if (dir == "desc"){
            if (Number(x.innerHTML) < Number(y.innerHTML)) {
                shouldSwitch = true;
            break;
            }
        }
      }
    
      if (shouldSwitch) {
        rows[i].parentNode.insertBefore(rows[i + 1], rows[i]);
        switching = true;
        switchcount ++;
      } else {
        if (switchcount == 0 && dir == "asc") {
            dir = "desc";
            switching = true;
        }
      }
    }
    let items = document.getElementsByClassName("No");
    for (var i = 0; i < items.length; i++) {
        items[i].innerHTML = no;
        no++;
    }
  }

function displayDetail(id) {
    var id = id;
    var name;
    var status;
    var address = "";
    var category = "";
    var phone;
    var price;
    var transactions = "";
    var url;
    var photos;
    var detail = document.getElementById("detail");
    $.ajax({
        type: 'GET',
        url: 'https://static-welder-362708.wl.r.appspot.com/business/' + id,
        success: function (response) {
            document.getElementById('detail').style.backgroundColor = "white";
            document.getElementById("detail").style.display = "block";
            let result = JSON.parse(response);
            //console.log(result.hours[0].is_open_now);
            if (result.hasOwnProperty('name') && result.name !== ""){
                name = result.name;
            } else {
                name = "undefined";
            }
            if (result.hasOwnProperty('hours') && result.hours[0].is_open_now !== ""){
                status = result.hours[0].is_open_now;
            } else {
                status = "undefined";
            }
            //console.log(status);
            if (result.hasOwnProperty('display_address') && Object.keys(result.display_address).length !== 0){
                for (let x in result.display_address){
                    address += x;
                    address += " ";
                }
            } else {
                address = "undefined";
            }
            if (result.hasOwnProperty('categories') && Object.keys(result.categories).length !== 0){
                let cat_len = Object.keys(result.categories).length;
                category += result.categories[0].title;
                for (var i = 1; i < cat_len; i++){
                    category += " | ";
                    category += result.categories[i].title
                }
            } else {
                category = "undefined"
            }
            if (result.hasOwnProperty('display_phone') && result.display_phone !== ""){
                phone = result.display_phone;
            } else {
                phone = "undefined";
            }
            if (result.hasOwnProperty('price') && result.price !== ""){
                price = result.price;
            } else {
                price = "undefined";
            }
            if (result.hasOwnProperty('transactions') && Object.keys(result.transactions).length !== 0){
                let tran_len = Object.keys(result.transactions).length;
                let trans = result.transactions
                for (var i = 0; i < tran_len; i++){
                    transactions += trans[i];
                    transactions += " ";
                }
            } else {
                transactions = "undefined";
            }
            if (result.hasOwnProperty('url') && result.url !== ""){
                url = result.url;
            } else {
                url = "undefined";
            }

            if (result.hasOwnProperty('photos') && result.url !== []){
                photos = result.photos;
            } else {
                photos = "undefined";
            }

            let Bname = document.createElement('h2');
            if (name != "undefined"){
                Bname.innerHTML = name;
                Bname.setAttribute('class', 'bname');
            }
            let hr = document.createElement('hr');
            hr.setAttribute('width', '90%');

            let parent = document.createElement('div');
            parent.setAttribute('class', 'parent');

            let div_1 = document.createElement('div');
            if (status != "undefined"){
                div_1.setAttribute('class', 'child');
                let div_1a = document.createElement('div');
                div_1a.setAttribute('id', 'test');
                if (status === true) {
                    div_1a.innerHTML = "<h3>Open Now</h3>";
                    div_1a.style.backgroundColor = 'green';
                } else {
                    div_1a.innerHTML = "<h3>Closed</h3>";
                    div_1a.style.backgroundColor = 'red';
                }
                div_1.innerHTML = "<h2>Status</h2>";
                div_1.append(div_1a);
            }

            let div_2 = document.createElement('div');
            if (category != "undefined"){
                div_2.setAttribute('class', 'child');
                div_2.innerHTML = "<h2>Category</h2><h3>" + category + "</h3>";
            }

            let div_3 = document.createElement('div');
            if (address != "undefined"){
                div_3.setAttribute('class', 'child');
                div_3.innerHTML = "<h2>Address</h2><h3>" + address + "</h3>";
            }

            let div_4 = document.createElement('div');
            if (phone != "undefined"){
                div_4.setAttribute('class', 'child');
                div_4.innerHTML = "<h2>Phone Number</h2><h3>" + phone + "</h3>";
            }

            let div_5 = document.createElement('div');
            if (transactions != "undefined"){
                div_5.setAttribute('class', 'child');
                div_5.innerHTML = "<h2>Transactions Supported</h2><h3>" + transactions + "</h3>";
            }

            let div_6 = document.createElement('div');
            if (price != "undefined"){
                div_6.setAttribute('class', 'child');
                div_6.innerHTML = "<h2>Price</h2><h3>" + price + "</h3>";
            }

            let div_7 = document.createElement('div');
            if (url != "undefined"){
                div_7.setAttribute('class', 'child');
                div_7.innerHTML = "<h2>More info</h2><h3>" + "<a href=" + url + ">" + "yelp" + "</h3>" + "</h3>";
            }

            let div_8 = document.createElement('div');
            let photo_len = Object.keys(result.photos).length;
            if (photos != "undefined"){
                div_8.setAttribute('class', 'photos');
                for (var i = 0; i < photo_len; i++) {
                    let div = document.createElement('div');
                    let x = document.createElement('img');
                    x.setAttribute('src', photos[i]);
                    //x.style.width = '150px';
                    div.append(x)
                    div_8.append(div);
                }
            }

            let div_9 = document.createElement('div');
            if (photos != "undefined"){
                div_9.setAttribute('class', 'photos');
                for (var i = 1; i < photo_len + 1; i++) {
                    let div = document.createElement('div');
                    div.setAttribute('id', 'imageNum');
                    let imageNum = document.createElement('h2');
                    imageNum.setAttribute('class', 'imageNum');
                    imageNum.innerHTML = "image" + i;
                    imageNum.style.textAlign = "center";
                    div.append(imageNum);
                    div_9.append(div);
                }
            }

            
            if (div_1.innerHTML !== ""){
                parent.append(div_1);
            }
            if (div_2.innerHTML !== ""){
                parent.append(div_2);
            }
            if (div_3.innerHTML !== ""){
                parent.append(div_3);
            }
            if (div_4.innerHTML !== ""){
                parent.append(div_4);
            }
            if (div_5.innerHTML !== ""){
                parent.append(div_5);
            }
            if (div_6.innerHTML !== ""){
                parent.append(div_6);
            }
            if (div_7.innerHTML !== ""){
                parent.append(div_7);
            }

            detail.append(Bname);
            detail.append(hr);
            detail.append(parent);
            if (div_8.innerHTML !== ""){
                detail.append(div_8);
            }
            if (div_9.innerHTML !== ""){
                detail.append(div_9);
            }


        }
    });
}

function clearInfo() {
    document.getElementById('keyword').value = '';
    document.getElementById('distance').value = '';
    document.getElementById('location').value = '';
    document.getElementById('category').value = "Default";
    document.getElementById('table_area').innerHTML = "";
    document.getElementById('detail').innerHTML = "";
    document.getElementById('detail').style.backgroundColor = "lightgray";
    document.getElementById('autogeo').checked = false;
    document.getElementById('location').disabled = false;
}

$(document).change(function() {
    if (document.getElementById('autogeo').checked) {
        document.getElementById('location').value = '';
        document.getElementById('location').disabled = true;
    } else {
        document.getElementById('location').disabled = false;
    }
})
