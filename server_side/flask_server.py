import flask
import werkzeug
import time
import pprint
import json
from tinydb import TinyDB, Query
import calendar

app = flask.Flask(__name__)

log_file = open("stuff.txt","a")

db = TinyDB('db.json')

@app.route('/list_cats', methods = ['GET'])
def list_cats():
    return json.dumps(db.all()), 200, {'ContentType':'application/json'}

@app.route('/list_recent_cats', methods = ['GET'])
def list_recent_cats():
    Cats = Query()
    hours = 6
    if flask.request.args.get('hours'):
        hours = int(flask.request.args.get('hours'))
    yesterday = calendar.timegm(time.gmtime()) - (60 * 60 * hours)
    qr = db.search(Cats.uploadedAt >= yesterday)
    return json.dumps(qr), 200, {'ContentType':'application/json'}

# Upload image
@app.route('/upload', methods = ['GET', 'POST'])
def handle_request():
    print("Handling /upload...")

    files_ids = list(flask.request.files)

    print("Request headers - ")
    print(flask.request.headers)

    print("Files are ")
    pprint.pprint(flask.request.files)

    print("Fields are ")
    pprint.pprint(flask.request.form)

    filePath = None
    fileName = None
    catName=flask.request.form['name']
    caption=flask.request.form['caption']
    lat=float(flask.request.form['latitude'])
    lon=float(flask.request.form['longitude'])


    # log received data
    print("\nNumber of Received Images : "+ str(len(files_ids)))
    print("Caption: "+caption)

    image_num = 1

    for file_id in files_ids:
        print("\nSaving Image %s/%d" % (str(image_num), len(files_ids)))
        imagefile = flask.request.files[file_id]
        filename = werkzeug.utils.secure_filename(imagefile.filename)
        print("Image Filename : " + imagefile.filename)
        timestr = time.strftime("%Y%m%d-%H%M%S")
        filePath = "Images/"+timestr+'_'+filename
        fileName = timestr+'_'+filename
        imagefile.save(filePath)
        image_num = image_num + 1
        break
    db.insert({
        "filePath": filePath,
        "name": catName,
        "caption": caption,
        "fileName": fileName,
        "latitude" : lat,
        "longitude" : lon,
        "uploadedAt": calendar.timegm(time.gmtime())
    })

    print("\n")
    print("Image(s) Uploaded Successfully. Come Back Soon.")
    return json.dumps({'success':True}), 200, {'ContentType':'application/json'}

if __name__ == "__main__":
    print("Running server...")
    app.run('0.0.0.0',5000)