# Money Matters

**Money Matters** is a Spring Boot web application that helps people understand the economy and manage their own money. It brings together live economic news, macroeconomic data, a budget tracker, a stock-market investment simulator and a cost-of-production tool for firms, all in one place, with local AI models to explain things in plain English.

It was built as an A-Level Computer Science NEA project, in response to research (a survey of 21 sixth-formers, an interview with an economics teacher, and secondary data from the Foresters Friendly Society) showing that many young people find economic information confusing, biased or overwhelming.

---

## Contents

- [Features](#features)
- [Tech stack](#tech-stack)
- [Architecture](#architecture)
- [Algorithms and mathematics](#algorithms-and-mathematics)
- [Project structure](#project-structure)
- [Getting started](#getting-started)
- [Testing](#testing)
- [Data sources](#data-sources)
- [Limitations and future work](#limitations-and-future-work)
- [Documentation](#documentation)

---

## Features

The app has two types of user, **Consumers** and **Producers**, each with their own tools.

| Feature | Consumer | Producer | Description |
|---|:---:|:---:|---|
| 📰 **Economic News** | ✅ | ✅ | Pulls articles from BBC, The Telegraph, The Economist, The Independent and CNBC via RSS. Sort by date or headline, search by headline, and get an AI-generated summary of any article. |
| 📊 **Economic Figures** | ✅ | ✅ | Real GDP, GDP per capita, inflation, unemployment, interest rates and more from the World Bank API, plotted as interactive charts. |
| 💱 **Exchange Rates** | ✅ | ✅ | Live and historical exchange rates plus a currency converter, using the Frankfurter API. |
| 🗳️ **Rate the Economy** | ✅ | | Answer a fixed set of questions about your own economic situation and see how other consumers feel, shown as pie charts by date. |
| 📈 **Investment Simulator** | ✅ | ✅ | Buy and sell real stocks with virtual money (starting at £10,000). View price history, volume, and 10/50/100-day Simple Moving Averages. If you lose everything, you get £1,000 to start again. |
| 🧾 **Budget Tracker** | ✅ | | Set a budget, log purchases, see a spending breakdown, get a sustainable daily spending figure, a 12-month budget forecast based on inflation, and warnings about unusually risky purchases. |
| 🏭 **Cost of Production** | | ✅ | Create, read, update and delete factor inputs (labour, capital, land), view cost/output/revenue graphs, undo mistakes, and get advice on the most profitable combination of inputs within a cost limit. |
| 🔐 **Secure Login** | ✅ | ✅ | Registration with password rules and unique emails per role. Passwords are stored encrypted using a Vernam cipher. |
| 🧑‍🏫 **Dashboard** | ✅ | ✅ | A searchable glossary of economic terms and an AI assistant (Ollama) to answer economics questions. |

---

## Tech stack

**Backend**
- Java 21, Spring Boot 3.5.5 (Spring Web, Thymeleaf, Actuator)
- Jsoup for parsing RSS/XML feeds
- Jackson for JSON
- JUnit and Mockito for testing

**Python microservice** (Flask, runs alongside the Java app)
- Flask for the JSON API
- NumPy for vector and matrix maths
- yfinance for stock data
- Ollama (`gemma3:4b` and `granite3.1-moe:1b`) for local AI
- newspaper3k for extracting article text

**Database**
- Microsoft SQL Server (SQL Server Express)

**Frontend**
- HTML, CSS and JavaScript served through Thymeleaf templates
- Plotly.js for charts

---

## Architecture

```
┌──────────────┐     ┌─────────────────────────────┐     ┌──────────────────┐
│   Browser    │◄───►│  Spring Boot app (:2207)    │◄───►│  SQL Server      │
│ Thymeleaf UI │     │  Controllers → Logic → Repo │     │  MONEYMATTERSDB  │
└──────────────┘     └──────┬───────────────┬──────┘     └──────────────────┘
                            │               │
                            │ REST          │ HTTP/JSON
                            ▼               ▼
              ┌──────────────────────┐   External APIs
              │ Flask service (:5000)│   • World Bank
              │ • Budget / CoP maths │   • Frankfurter
              │ • yfinance stock data│   • News RSS feeds
              │ • Ollama AI          │
              └──────────────────────┘
```

The Java side follows a layered design:

- **Controllers** handle HTTP requests and render Thymeleaf pages.
- **Logic** holds the business rules (budgeting, investing, rating the economy, web scraping).
- **Repository** contains the SQL (DDL, DML and DQL) for each table.
- **Records / Users** model the data, with `User` as an interface implemented by `Consumer` and `Producer`.
- **PythonFlaskAPI** contains a thread-safe queue (`APIQueue`) and worker (`APIWorker`) that call the Flask service concurrently.

---

## Algorithms and mathematics

| Area | Technique |
|---|---|
| Password security | **Vernam cipher** (one-time pad); keys are stored separately in JSON files |
| Sorting | **Recursive merge sort** (news, spending) |
| Searching | **Recursive binary search** (news headlines, purchases) |
| Risky purchases | Per-item **mean, standard deviation and z-score**; purchases with \|z\| > 1.5 are flagged |
| Budget forecast | **Exponential growth** driven by inflation, with monthly rate `r_m = (1 + r)^(1/12) − 1`, computed using **vectors and matrices** (`F = G × I`) |
| Production output | **Cobb-Douglas production function**, `Y = A · K^α · L^β` |
| Optimal inputs | **Combinatorial subset search** over the powerset of factor inputs to maximise profit under a cost constraint |
| Stock trends | **Simple Moving Averages** (10/50/100-day) and **linear regression** |
| Performance | **Multithreading with queues** for fetching stock data and parsing spending history |
| Undo | **Stack** operations in the cost-of-production tool |

---

## Project structure

```
Money Matters Code/
├── MoneyMatters/                      # Maven / Spring Boot project
│   ├── pom.xml
│   └── src/
│       ├── main/java/com/mycompany/
│       │   ├── Algorithms/            # Merge sort, binary search, Vernam cipher, file handling
│       │   ├── Controllers/           # One controller per feature
│       │   ├── EconomicFigures/       # World Bank + exchange-rate API clients and fetchers
│       │   ├── Logic/                 # Business logic
│       │   ├── MoneyMatters/          # Application entry point
│       │   ├── PythonFlaskAPI/        # Flask service (.py) and Java client (.java)
│       │   ├── Records/               # Java records
│       │   ├── Repository/            # SQL queries per table
│       │   └── Users/                 # User, Consumer, Producer
│       ├── main/resources/
│       │   ├── static/                # CSS and images
│       │   ├── templates/             # Thymeleaf HTML pages
│       │   └── application.properties
│       └── test/java/                 # JUnit tests
├── SQLCreateTableQueries/             # Table creation scripts
├── SQLSelectTableQueries/
├── glossaryOfEconomicTerms.json       # Glossary data
├── rateTheEconomyQuestions.json       # Economy-rater questions
├── tickerInfo.json                    # Supported stocks
├── UserOpinions/                      # Rating answers (generated at runtime)
└── VernamKeys/                        # Cipher keys (generated at runtime)
```

---

## Getting started

### Prerequisites

- **JDK 21** and **Maven**
- **Python 3.11+**
- **SQL Server Express** (instance `SQLEXPRESS` on port `1433`, with SQL authentication enabled)
- **[Ollama](https://ollama.com/)** for the AI features

### 1. Clone the repository

```bash
git clone https://github.com/<your-username>/money-matters.git
cd money-matters
```

### 2. Set up the database

1. Create a database named `MONEYMATTERSDB` on your local SQL Server instance.
2. Create a SQL login for the app and give it access to that database.
3. Run the scripts in `SQLCreateTableQueries/` in this order, so foreign keys resolve:
   `Consumers` → `Producers` → `Goods` → `Stocks` → `Purchase` → `FactorInput` → `EconomyRater` → `Trades`

The connection settings are in `Repository/SQLQueries.java`:

```java
protected static final String url = "jdbc:sqlserver://localhost\\SQLEXPRESS:1433;databaseName=MONEYMATTERSDB;encrypt=true;trustServerCertificate=true;";
protected static final String databaseUser = "...";
protected static final String databasePassword = "...";
```

> ⚠️ **Never commit real credentials.** Replace these with your own local values, and ideally move them to environment variables or `application.properties` excluded from version control.

### 3. Set up the Python service

```bash
cd MoneyMatters/src/main/java/com/mycompany/PythonFlaskAPI
python -m venv venv
source venv/bin/activate          # Windows: venv\Scripts\activate
pip install flask numpy yfinance ollama newspaper3k
python PythonFlaskAPI.py          # runs on http://localhost:5000
```

### 4. Set up Ollama

```bash
ollama pull gemma3:4b             # Q&A assistant
ollama pull granite3.1-moe:1b     # news summaries
```

Smaller models are used on purpose so the app runs on modest hardware.

### 5. Run the Spring Boot app

The app reads `glossaryOfEconomicTerms.json` and writes to `VernamKeys/` and `UserOpinions/` using relative paths, so **run it from the `Money Matters Code` root folder**:

```bash
cd "Money Matters Code"
mvn -f MoneyMatters/pom.xml spring-boot:run
```

Then open **http://localhost:2207** in your browser.

---

## Testing

```bash
mvn -f MoneyMatters/pom.xml test
```

The test suite covers:

- `VernamCipherTest`: encryption and decryption
- `DDLTest` and `ConsumerTableTest`: database schema and consumer queries
- `CostRestraintTest` and `CostRestraintTestRandom`: the optimal factor-input search
- `WorldBankAPITest`: API client behaviour
- `PythonFlaskAPITest`: Java ↔ Flask integration

> Database and API tests need SQL Server and the Flask service running.

---

## Data sources

| Source | Used for |
|---|---|
| [World Bank API](https://data.worldbank.org/) | GDP, GDP per capita, inflation, unemployment, interest rates, trade, tax revenue, reserves, Gini index, government debt |
| [Frankfurter API](https://www.frankfurter.app/) | Exchange rates and currency conversion |
| [yfinance](https://github.com/ranaroussi/yfinance) | Stock prices, company info, historical data |
| RSS feeds | BBC News, The Telegraph, The Economist, The Independent, CNBC |

---

## Limitations and future work

**Known limitations**
- The system does not predict future economic conditions or stock prices.
- AI answers come from small local models (1B and 4B parameters), so quality and speed depend on your hardware.
- The investment simulator uses virtual money only and is for learning, not financial advice.

**Planned improvements**
- User-defined news sources
- Machine-learning or regression-based economic forecasts
- Randomised, context-aware questions for the economy rater
- Comparing stocks with other assets (bonds, property, savings)
- Scenario simulations for firms (price shocks, productivity, demand and supply changes)

---

## Documentation

The full project write-up (analysis, design, flowcharts, ER diagrams, class diagrams, SQL, testing and evaluation) is in 
[`Money Matters Documentation.pdf`](./Money%20Matters%20Documentation.pdf)

## Author
**Tharun Nadeswaran**
