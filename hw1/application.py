from flask import Flask,render_template,flash,redirect,url_for,session,logging,request

app = Flask(__name__)

@app.route("/")
def index():
    return render_template("test.html", qr_image = "")

@app.route('/', methods=['POST'])
def my_form_post():
    text = request.form['qrname']
    qrcolour = request.form['qrList']
    background = request.form['backgroundList']
    url = 'https://api.qrserver.com/v1/create-qr-code/?size=150x150&data=' + str(text) + '&color=' + str(qrcolour) + '&bgcolor=' + str(background) 
    return render_template("test.html", qr_image = url)

if __name__ == "__main__":
    # get the status of the response
    app.run()
