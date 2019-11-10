
# # A very simple Flask Hello World app for you to get started with...

# from flask import Flask

# app = Flask(__name__)

# @app.route('/')
# def hello_world():
#     with open("mysite/stuff.txt") as f:
#         info = f.read()
#     return info

import flask
import werkzeug
import time

app = flask.Flask(__name__)

log_file = open("mysite/stuff.txt","a")

# Check server is running
@app.route('/test', methods = ['GET', 'POST'])
def test():
    return "Successful connection"

# Check file can be received
@app.route("/print_filename", methods=['POST','PUT'])
def print_filename():
    file = flask.request.files['file']
    filename=werkzeug.utils.secure_filename(file.filename)
    return filename

# Upload image
@app.route('/upload', methods = ['GET', 'POST'])
def handle_request():
    files_ids = list(flask.request.files)
    # caption=flask.request.form['caption']

    # log received data
    log_file.write("\nNumber of Received Images : "+ str(len(files_ids)))
    # log_file.write("Caption: "+caption)

    image_num = 1
    for file_id in files_ids:
        log_file.write("\nSaving Image ", str(image_num), "/", len(files_ids))
        imagefile = flask.request.files[file_id]
        filename = werkzeug.utils.secure_filename(imagefile.filename)
        log_file.write("Image Filename : " + imagefile.filename)
        timestr = time.strftime("%Y%m%d-%H%M%S")
        imagefile.save("Images/"+timestr+'_'+filename)
        image_num = image_num + 1
    print("\n")
    return "Image(s) Uploaded Successfully. Come Back Soon."