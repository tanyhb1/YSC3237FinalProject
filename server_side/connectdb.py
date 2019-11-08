
import psycopg2
from flask import Flask, escape, request, jsonify



app = Flask(__name__)


@app.route('/')
def root():
    return "This is the SnapCat API"

@app.route('/users')
def getUsers():
    conn = psycopg2.connect("dbname=photobase user=manager password=manager host=localhost")
    cur = conn.cursor()
    cur.execute("SELECT * FROM users;")
    users = cur.fetchall()
    cur.close()
    conn.close()
    return jsonify(users)



@app.route('/addUser')
def addUser():
    conn = psycopg2.connect("dbname=photobase user=manager password=manager host=localhost")
    cur = conn.cursor()

    name = request.args.get("name", type=str)
    command = "INSERT INTO users(photo) VALUES ('" +name+ "', 'default_photo');"
    cur.execute(command)
    conn.commit()

    cur.close()
    conn.close()
    return "Added user\n"


@app.route('/addPhoto')
def addPhoto():
    conn = psycopg2.connect("dbname=photobase user=manager password=manager host=localhost")
    cur = conn.cursor()

    lon = request.args.get("long", type=str)
    lat = request.args.get("lat", type=str)
    name = request.args.get("name", type=str)
    print lon
    print lat
    print name
    command = "SELECT (id) FROM users WHERE name='"+name+"'" 
    cur.execute(command)
    ix = cur.fetchone()[0]
    command = "INSERT INTO photos(long, lat, photo, creator) VALUES ('" +lon+ "','" +lat+ "', 'default_photo', '" +str(ix)+ "');"
    cur.execute(command)
    conn.commit()

    cur.close()
    conn.close()
    return "Added photo\n"



@app.route('/recentPhotos')
def getRecentPhotos():
    conn = psycopg2.connect("dbname=photobase user=manager password=manager host=localhost")
    cur = conn.cursor()

    mins = request.args.get("minutes", type=str)
    if mins == None:
        mins = "60"
    command = "SELECT * FROM photos where created_at > now() - interval '"+mins+" minute';"
    cur.execute(command)
    photos = cur.fetchall()

    cur.close()
    conn.close()
    return jsonify(photos)


if __name__ == "__main__":
    app.run(host='0.0.0.0', port=5000)

# TODO: expose port on DO
# https://stackoverflow.com/questions/51199475/flask-application-cannot-be-exposed-on-droplet