
import flask
import werkzeug
import time

app = flask.Flask(__name__)



@app.route('/test', methods = ['GET', 'POST'])
def test():
    return "Successful connection"

@app.route("/print_filename", methods=['POST','PUT'])
def print_filename():
    file = flask.request.files['file']
    filename=werkzeug.utils.secure_filename(file.filename)
    return filename

@app.route('/', methods = ['GET', 'POST'])
def handle_request():
    files_ids = list(flask.request.files)
    print("\nNumber of Received Images : ", len(files_ids))
    image_num = 1
    for file_id in files_ids:
        print("\nSaving Image ", str(image_num), "/", len(files_ids))
        imagefile = flask.request.files[file_id]
        filename = werkzeug.utils.secure_filename(imagefile.filename)
        print("Image Filename : " + imagefile.filename)
        timestr = time.strftime("%Y%m%d-%H%M%S")
        imagefile.save("Images/"+timestr+'_'+filename)
        image_num = image_num + 1
    print("\n")
    return "Image(s) Uploaded Successfully. Come Back Soon."


