from flask import Flask, render_template

#cd C:\Users\sadiq\Documents\Study Material\Y2T2\2107 Distributed Systems Programming\ICT2107_Project
#cd C:\Users\sadiq\Desktop\2107_Team1
#cd C:\Users\sadiq\Documents\GitHub\2107_Team1
#python website.py

app = Flask(__name__)

@app.route('/')
def home():
	return render_template('home.html')

@app.route('/covidcasesdata')
def covidcases():
	return render_template('covidcasesdata.html')

@app.route('/vaccinationdata')
def vaccination():
	return render_template('vaccinationdata.html')


@app.route('/tweetanalysis')
def tweet():
	return render_template('tweetanalysis.html')

if __name__=='__main__':
	app.run(debug=True)

	