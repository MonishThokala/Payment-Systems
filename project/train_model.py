import pandas as pd
from sklearn.ensemble import RandomForestClassifier
import joblib

print("Loading data...")
data = pd.read_csv("fraud_data.csv")

X = data.drop("isFraud", axis=1)
y = data["isFraud"]

print("Training model...")
model = RandomForestClassifier(n_estimators=100)
model.fit(X, y)

joblib.dump(model, "fraud_model.pkl")
print("Model saved as fraud_model.pkl")
