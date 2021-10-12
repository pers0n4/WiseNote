.PHONY: dev
dev:
	uvicorn app.main:app --reload --reload-dir app --reload-dir database

.PHONY: prod
prod:
	nohup uvicorn app.main:app --workers 2 --host 0.0.0.0 --port 80 > /dev/null 2>&1 &
