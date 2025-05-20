# AI PDF Query Application

This is a full-stack AI-powered application built with **Spring Boot** (backend) and **Angular** (frontend) that allows users to **upload PDF documents** and **query** about their contents. 
The application leverages **Google Vertex AI** for LLM and embeddings, **Amazon S3** for file storage, and **pgvector + PostgreSQL** for storing and querying vector embeddings.

---

## ✨ Features

* 📄 **Upload PDFs**: Users can upload one or more PDF documents securely.
* 🧠 **AI-Powered Querying**: Ask natural language questions about the contents of uploaded PDFs.
* 🔍 **Semantic Search**: Accurate and contextual search using Vertex AI embeddings and pgvector.
* ⛅ **Cloud-Based Storage**: PDFs are stored in Amazon S3 buckets.
* ⚙️ **Modular Backend**: Built using Spring Boot with integration to Vertex AI and pgvector.
* 💊 **Responsive Frontend**: Angular-powered user interface for easy interaction and querying.

---

## 🚀 Technologies Used

* **Backend**: Spring Boot
* **Frontend**: Angular
* **AI/ML**: Google Vertex AI (LLM + Embedding Model)
* **Storage**: Amazon S3
* **Vector DB**: pgvector extension on PostgreSQL

---

## 💡 Future Goals & Features

* 🔹 **User Authentication**: Support for user accounts and access control.
* 🔹 **Multi-document Context**: Querying across multiple documents simultaneously.
* 🔹 **Chat History**: Store and view past questions and answers.
* 🔹 **Advanced PDF Parsing**: Better handling of tables, images, and scanned content.
* 🔹 **Realtime Notifications**: Updates when indexing and processing is complete.
* 🔹 **Document Sharing**: Share access to documents and their insights.

---

## 📊 How It Works

1. **Upload**: User uploads a PDF via the Angular frontend.
2. **Storage**: File is saved to Amazon S3.
3. **Processing**: Text is extracted, chunked, and embedded using Vertex AI.
4. **Vector Storage**: Embeddings are stored in PostgreSQL using the pgvector extension.
5. **Query**: User submits a question, which is transformed into an embedding and matched against the stored vectors.
6. **Response**: The most relevant chunks are passed to Vertex AI LLM to generate a contextual answer.

---

## ⚡ Getting Started

> Instructions for cloning, building, and running the app can be added here.

```bash
TBD
```

---

## 🙌 Contributions

Pull requests are welcome! For major changes, please open an issue first to discuss what you would like to change.

---

## ✉️ Contact

For questions or feedback, please reach out at [your-email@example.com](mailto:your-email@example.com)

---

## 🙏 Acknowledgements

* Google Cloud Vertex AI
* pgvector
* Amazon S3
* Spring Boot & Angular communities
