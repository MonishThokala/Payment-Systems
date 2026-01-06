from fastapi import FastAPI
import joblib

app = FastAPI()
model = joblib.load("fraud_model.pkl")

@app.post("/predict")
def predict(features: dict):
    values = list(features.values())
    score = model.predict_proba([values])[0][1]
    return {"fraudScore": float(score)}
