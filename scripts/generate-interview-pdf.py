from reportlab.lib import colors
from reportlab.lib.pagesizes import A4
from reportlab.lib.styles import getSampleStyleSheet, ParagraphStyle
from reportlab.lib.units import mm
from reportlab.platypus import SimpleDocTemplate, Paragraph, Spacer, ListFlowable, ListItem


OUT_PATH = r"C:\Users\Anamika Patel\Desktop\payments\FX_Wallet_Technology_Interview_Prep.pdf"


def p(text, style):
    return Paragraph(text, style)


def bullets(items, style):
    return ListFlowable(
        [ListItem(Paragraph(item, style), leftIndent=6) for item in items],
        bulletType="bullet",
        start="circle",
        leftIndent=16,
        bulletFontSize=7,
    )


styles = getSampleStyleSheet()
styles.add(ParagraphStyle(
    name="TitleCustom",
    parent=styles["Title"],
    fontName="Helvetica-Bold",
    fontSize=24,
    leading=29,
    textColor=colors.HexColor("#0f172a"),
    spaceAfter=8,
))
styles.add(ParagraphStyle(
    name="Subtitle",
    parent=styles["BodyText"],
    fontSize=10.5,
    leading=15,
    textColor=colors.HexColor("#475569"),
    spaceAfter=10,
))
styles.add(ParagraphStyle(
    name="Heading",
    parent=styles["Heading2"],
    fontName="Helvetica-Bold",
    fontSize=14,
    leading=18,
    textColor=colors.HexColor("#14532d"),
    borderColor=colors.HexColor("#d8e3dc"),
    borderWidth=0,
    borderPadding=0,
    spaceBefore=13,
    spaceAfter=5,
))
styles.add(ParagraphStyle(
    name="SubHeading",
    parent=styles["Heading3"],
    fontName="Helvetica-Bold",
    fontSize=11,
    leading=14,
    textColor=colors.HexColor("#0f172a"),
    spaceBefore=8,
    spaceAfter=3,
))
styles.add(ParagraphStyle(
    name="BodyCustom",
    parent=styles["BodyText"],
    fontSize=9.7,
    leading=13.4,
    textColor=colors.HexColor("#17201b"),
    spaceAfter=5,
))
styles.add(ParagraphStyle(
    name="Callout",
    parent=styles["BodyText"],
    fontSize=9.8,
    leading=13.5,
    textColor=colors.HexColor("#0f172a"),
    backColor=colors.HexColor("#f0fdf4"),
    borderColor=colors.HexColor("#86efac"),
    borderWidth=0.8,
    borderPadding=7,
    spaceBefore=6,
    spaceAfter=8,
))
styles.add(ParagraphStyle(
    name="Answer",
    parent=styles["BodyText"],
    fontSize=9.5,
    leading=13.2,
    textColor=colors.HexColor("#0f172a"),
    backColor=colors.HexColor("#f8fafc"),
    borderColor=colors.HexColor("#dbe4ee"),
    borderWidth=0.6,
    borderPadding=7,
    spaceBefore=5,
    spaceAfter=8,
))

body = styles["BodyCustom"]
story = []

story.append(p("Resume and Interview Preparation", ParagraphStyle(
    "Eyebrow", parent=body, fontName="Helvetica-Bold", fontSize=8.5,
    textColor=colors.HexColor("#15803d"), leading=11, spaceAfter=3)))
story.append(p("FX Wallet: Technology Choices and Interview Answers", styles["TitleCustom"]))
story.append(p(
    "A prepared explanation for why this cross-border payments platform uses Java, Spring Boot, React, PostgreSQL, JWT, WebSocket, and Docker, including alternatives and interview-ready reasoning.",
    styles["Subtitle"],
))
story.append(p("<b>Main interview framing:</b> I chose this stack because it fits fintech requirements: secure financial workflows, reliable backend architecture, consistent wallet data, fast user experience, real-time updates, and deployment readiness.", styles["Callout"]))

sections = [
    ("Project Context", [
        ("para", "This project simulates a real-world cross-border payments platform similar to Wise or Revolut. Users can hold multi-currency wallets, convert funds, send money internationally, view transaction history, and receive fraud or payment alerts."),
        ("bullets", [
            "Security matters because users authenticate and perform payment actions.",
            "Data consistency matters because wallet balances and transactions must remain correct.",
            "Real-time feedback matters because payment and fraud statuses should be visible immediately.",
            "Maintainability matters because payment systems grow into domains such as user, wallet, payment, exchange-rate, and fraud services.",
        ]),
    ]),
    ("Java", [
        ("para", "<b>Why chosen:</b> Java is mature, strongly typed, stable, and widely used in banking, fintech, and enterprise systems."),
        ("bullets", [
            "Strong typing helps reduce runtime errors in financial calculations and domain models.",
            "Excellent ecosystem for security, REST APIs, databases, testing, and monitoring.",
            "Good long-term maintainability for large backend systems.",
            "Common in payment companies, banks, and enterprise platforms.",
        ]),
        ("para", "<b>Why not Node.js:</b> Node.js is excellent for lightweight APIs and real-time apps, but Java is often preferred for complex financial domain logic because of type safety, mature enterprise tooling, and predictable backend structure."),
        ("para", "<b>Why not Python:</b> Python is great for data science, scripting, and ML, but Java is usually stronger for high-throughput, structured enterprise backend systems."),
        ("answer", "<b>Interview answer:</b> I chose Java because fintech systems need reliability, type safety, and long-term maintainability. Java is also widely used in banking and payment platforms, so it matches the domain well."),
    ]),
    ("Spring Boot", [
        ("para", "<b>Why chosen:</b> Spring Boot helps build production-ready Java APIs quickly while still supporting clean architecture."),
        ("bullets", [
            "Strong REST API support.",
            "Dependency injection for clean service design.",
            "Spring Security integration for authentication and authorization.",
            "JPA/Hibernate integration for persistence.",
            "Profiles for local, staging, and production configuration.",
            "Good support for validation, testing, and configuration management.",
        ]),
        ("para", "<b>Why not plain Java:</b> Plain Java would require too much manual setup for routing, security, persistence, validation, and configuration."),
        ("para", "<b>Why not Express.js:</b> Express is flexible and lightweight, but Spring Boot provides more standardized enterprise features out of the box."),
        ("answer", "<b>Interview answer:</b> I used Spring Boot because it gives a production-ready backend structure with less boilerplate. It supports REST APIs, security, database integration, validation, and environment-based configuration, all of which are important for a payments platform."),
    ]),
    ("React", [
        ("para", "<b>Why chosen:</b> React is a strong fit for an interactive fintech dashboard with wallet cards, forms, charts, tables, and real-time alerts."),
        ("bullets", [
            "Component-based UI makes wallet cards, transaction tables, and payment forms reusable.",
            "Works well with REST APIs and WebSocket updates.",
            "Large ecosystem for routing, forms, state management, and charts.",
            "Widely used and recognizable to recruiters/interviewers.",
        ]),
        ("para", "<b>Why not Angular:</b> Angular is good for enterprise apps, but it is heavier and more opinionated. React gave more flexibility for this project."),
        ("para", "<b>Why not Thymeleaf:</b> Server-rendered templates are fine for simpler pages, but this app needs a dynamic dashboard and real-time UI updates."),
        ("answer", "<b>Interview answer:</b> I chose React because the frontend is dashboard-heavy and interaction-heavy. React's component model made it easier to build reusable UI pieces like wallet cards, transaction tables, payment forms, and alert components."),
    ]),
    ("PostgreSQL", [
        ("para", "<b>Why chosen:</b> PostgreSQL provides reliable relational storage and ACID transactions, which are important for wallet and payment data."),
        ("bullets", [
            "Supports transactional consistency for payment workflows.",
            "Good relational modeling for users, wallets, payments, exchange rates, and audit history.",
            "Supports indexes, constraints, and complex queries.",
            "Open-source, production-grade, and widely used in fintech/SaaS systems.",
        ]),
        ("para", "<b>Why not MongoDB:</b> MongoDB is flexible, but payments and wallet balances are strongly relational and consistency-sensitive. A relational database is a better default choice."),
        ("para", "<b>Why not H2 only:</b> H2 is useful for local development, but PostgreSQL is more suitable for production-like deployment."),
        ("answer", "<b>Interview answer:</b> I chose PostgreSQL because financial data needs consistency and transactional integrity. Wallet balances, payment records, users, and audit history are relational, so PostgreSQL is a better fit than a document database."),
    ]),
    ("JWT", [
        ("para", "<b>Why chosen:</b> JWT supports stateless authentication between the React frontend and Spring Boot backend."),
        ("bullets", [
            "Works cleanly with REST APIs.",
            "Avoids server-side session storage.",
            "Can carry user identity and role claims.",
            "Fits distributed or microservice-style architecture.",
        ]),
        ("para", "<b>Why not server sessions:</b> Sessions are simple for traditional web apps, but distributed systems need shared session storage or sticky sessions."),
        ("para", "<b>Production note:</b> JWT should use HTTPS, strong signing secrets, short expiry, refresh-token rotation, and a revocation strategy."),
        ("answer", "<b>Interview answer:</b> I used JWT because the frontend and backend are separate applications. JWT allows stateless authentication, works cleanly with REST APIs, and supports role-based authorization without server-side session storage."),
    ]),
    ("WebSocket", [
        ("para", "<b>Why chosen:</b> WebSocket enables real-time payment status and fraud alert updates."),
        ("bullets", [
            "Server can push updates instantly to the browser.",
            "Useful for payment status changes and alerts.",
            "Avoids repeated polling requests.",
            "Improves dashboard user experience.",
        ]),
        ("para", "<b>Why not REST only:</b> REST is request-response. The frontend would need to refresh or poll to get updates."),
        ("para", "<b>Why not polling:</b> Polling is easier but inefficient because it sends repeated requests even when nothing changes."),
        ("answer", "<b>Interview answer:</b> I used WebSocket because payment platforms benefit from real-time updates. When a payment status changes or fraud alert is generated, the user should see it immediately without refreshing the page."),
    ]),
    ("Docker", [
        ("para", "<b>Why chosen:</b> Docker makes the project easier to run consistently across different machines and deployment environments."),
        ("bullets", [
            "Helps run backend, frontend, and database with predictable configuration.",
            "Reduces environment mismatch issues.",
            "Useful for deployment and CI/CD pipelines.",
            "Makes the project easier for reviewers to run.",
        ]),
        ("para", "<b>Why not manual setup only:</b> Manual setup works, but every developer must configure Java, Node, PostgreSQL, environment variables, and ports correctly."),
        ("para", "<b>Why not Kubernetes:</b> Kubernetes is powerful but too heavy for this project scale. Docker Compose is enough for local multi-service orchestration."),
        ("answer", "<b>Interview answer:</b> I used Docker because this project has multiple moving parts: backend, frontend, and database. Docker makes it easier to run the same setup locally or in deployment without environment mismatch."),
    ]),
    ("How The Stack Works Together", [
        ("answer", "<b>Strong answer:</b> I selected Java and Spring Boot for a reliable, secure backend; PostgreSQL for transactional consistency; React for a dynamic user dashboard; JWT for stateless authentication; WebSocket for real-time payment alerts; and Docker for consistent deployment. Together, these technologies match the needs of a fintech-style platform where security, consistency, maintainability, and user experience are important."),
    ]),
    ("Common Follow-Up Questions", [
        ("sub", "Why not full microservices?"),
        ("para", "I designed the project in a modular way so it can evolve into microservices, but I kept it simpler for the project scope. In a real production system, user, wallet, payment, exchange-rate, notification, and fraud services could be separated. For this version, modular Spring Boot layers give clarity without adding unnecessary operational complexity."),
        ("sub", "Is JWT secure?"),
        ("para", "JWT is secure when implemented correctly. Tokens should be signed with a strong secret or private key, have short expiration, be sent only over HTTPS, and refresh tokens should be stored securely. In production, I would add token revocation, refresh-token rotation, and monitoring."),
        ("sub", "Why PostgreSQL for wallets?"),
        ("para", "Wallet balances need strong consistency. PostgreSQL supports ACID transactions, row locking, constraints, and reliable relational modeling, which helps prevent inconsistent balances or duplicate transaction updates."),
        ("sub", "Why WebSocket instead of Kafka?"),
        ("para", "They solve different problems. Kafka is mainly backend-to-backend event streaming, while WebSocket is server-to-browser real-time communication. In production, Kafka could process internal payment and fraud events, and WebSocket could deliver selected updates to the user interface."),
    ]),
    ("Best Short Answer", [
        ("answer", "I chose this stack because it matches fintech requirements. Java and Spring Boot are reliable and enterprise-ready for secure backend services. PostgreSQL gives ACID consistency for wallet and payment data. React is suitable for a dynamic dashboard. JWT supports stateless authentication between frontend and backend. WebSocket enables real-time payment and fraud alerts. Docker makes the system easier to run and deploy consistently."),
    ]),
]

for heading, parts in sections:
    story.append(p(heading, styles["Heading"]))
    for kind, content in parts:
        if kind == "para":
            story.append(p(content, body))
        elif kind == "bullets":
            story.append(bullets(content, body))
        elif kind == "answer":
            story.append(p(content, styles["Answer"]))
        elif kind == "sub":
            story.append(p(content, styles["SubHeading"]))
    story.append(Spacer(1, 2 * mm))

doc = SimpleDocTemplate(
    OUT_PATH,
    pagesize=A4,
    rightMargin=16 * mm,
    leftMargin=16 * mm,
    topMargin=16 * mm,
    bottomMargin=16 * mm,
)
doc.build(story)
print(OUT_PATH)
