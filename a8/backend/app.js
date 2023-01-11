

const express = require('express')
const app = express();
const cors = require('cors')
const axios = require('axios');


app.use(cors({
    credentials:true,
    origin:["http://localhost:4200"]
}));

app.use(require('body-parser').json());

app.get("/api/test", (req, res) => {
    res.header("Access-Control-Allow-Origin", "*")
    res.status(200).send("hello").end();
})

app.get('/yelp/table/:lat/:long/:keyword/:category/:radius', function(req, res) {
    const lat = req.params.lat;
    const long = req.params.long;
    const keyword = req.params.keyword;
    const category = req.params.category;
    const radius = req.params.radius
    res.header("Access-Control-Allow-Origin", "*")
    axios.get('https://api.yelp.com/v3/businesses/search', {
        headers : {
            "Authorization" : "Bearer CGKSCSZlI8kl6zTK1ewXd6ZiGxfWcHMmphlgpMLfCWzAeelvSeYhdOog49kiD3Dzim1NoY2-H6Z1M86MawGBm1v7prp5f6KEQTNqLskML_oQtpSzuY-gC3NP5WE3Y3Yx"
        },
        params: {
            latitude: lat,
            longitude: long,
            term: keyword,
            categories: category,
            radius: radius
        }
    })
    .then((response) => {
        res.status(200).send(response.data.businesses.slice(0, 10))
    });
})
app.get('/yelp/search/:keyword', function(req, res) {
    const keyword = req.params.keyword
    res.header("Access-Control-Allow-Origin", "*")
    axios.get('https://api.yelp.com/v3/autocomplete', {
        headers : {
            "Authorization" : "Bearer CGKSCSZlI8kl6zTK1ewXd6ZiGxfWcHMmphlgpMLfCWzAeelvSeYhdOog49kiD3Dzim1NoY2-H6Z1M86MawGBm1v7prp5f6KEQTNqLskML_oQtpSzuY-gC3NP5WE3Y3Yx"
        },
        params: {
            text: keyword
        }
    })
    .then((response) => {
        res.status(200).send(response.data)
    });
})

app.get('/yelp/:id', function(req, res) {
    const id = req.params.id
    res.header("Access-Control-Allow-Origin", "*")
    axios.get('https://api.yelp.com/v3/businesses/' + id, {
        headers : {
            "Authorization" : "Bearer CGKSCSZlI8kl6zTK1ewXd6ZiGxfWcHMmphlgpMLfCWzAeelvSeYhdOog49kiD3Dzim1NoY2-H6Z1M86MawGBm1v7prp5f6KEQTNqLskML_oQtpSzuY-gC3NP5WE3Y3Yx"
        }
    })
    .then((response) => {
        res.status(200).send(response.data)
    });
})
app.get('/yelp/reviews/:id', function(req, res) { 
    const id = req.params.id
    res.header("Access-Control-Allow-Origin", "*")
    axios.get('https://api.yelp.com/v3/businesses/' + id + '/reviews', {
        headers : {
            "Authorization" : "Bearer CGKSCSZlI8kl6zTK1ewXd6ZiGxfWcHMmphlgpMLfCWzAeelvSeYhdOog49kiD3Dzim1NoY2-H6Z1M86MawGBm1v7prp5f6KEQTNqLskML_oQtpSzuY-gC3NP5WE3Y3Yx"
        }
    })
    .then((response) => {
        //console.log(response.data);
        res.status(200).send(response.data)
    });
})
app.get('/google/:address', function(req, res) { 
    const address = req.params.address
    res.header("Access-Control-Allow-Origin", "*")
    axios.get('https://maps.googleapis.com/maps/api/geocode/json?address=' + address + '&key=AIzaSyBu_WuRIabfiGyJniW8NUs5Zbjkx34eu_o')
    .then((response) => {
        //lat = response.data.results[0].geometry.location.lat;
        //lng = response.data.results[0].geometry.location.lng;
        res.status(200).send(response.data)
    });
})
const PORT = process.env.PORT || 8080;
app.listen(PORT, () => {
  console.log(`Server listening on port ${PORT}...`);
});

