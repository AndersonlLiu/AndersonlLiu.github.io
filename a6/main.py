from flask import Flask
import requests

app = Flask(__name__,static_url_path='')

@app.route('/')
def index():
    return app.send_static_file("a6.html")
    
@app.route('/business/<latitude>/<longtitude>/<keyword>/<categories>/<radius>')
def table(latitude, longtitude, keyword, categories, radius):
    url = 'https://api.yelp.com/v3/businesses/search?term=' + keyword + "&latitude=" + latitude + '&longitude=' + longtitude + '&categories=' + categories + '&radius=' + radius
    headers = {
        "Accept" : "application/json",
        "Authorization" : "Bearer CGKSCSZlI8kl6zTK1ewXd6ZiGxfWcHMmphlgpMLfCWzAeelvSeYhdOog49kiD3Dzim1NoY2-H6Z1M86MawGBm1v7prp5f6KEQTNqLskML_oQtpSzuY-gC3NP5WE3Y3Yx"
    }
    response = requests.request("GET", url, headers = headers)
    return response.text

@app.route('/business/<id>')
def detail(id):
    url = 'https://api.yelp.com/v3/businesses/' + id
    headers = {
        "Accept" : "application/json",
        "Authorization" : "Bearer CGKSCSZlI8kl6zTK1ewXd6ZiGxfWcHMmphlgpMLfCWzAeelvSeYhdOog49kiD3Dzim1NoY2-H6Z1M86MawGBm1v7prp5f6KEQTNqLskML_oQtpSzuY-gC3NP5WE3Y3Yx"
    }
    response = requests.get(url, headers=headers)
    return response.text

if __name__ == '__main__':
    app.run(host='127.0.0.1', port=8080, debug=True)


