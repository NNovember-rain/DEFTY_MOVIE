# DEFTY_MOVIE
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>DEFTY_MOVIE README</title>
  <style>
    body {
      font-family: 'Arial', sans-serif;
      background-color: #f4f4f4;
      margin: 0;
      padding: 0;
    }
    .container {
      width: 80%;
      margin: 0 auto;
      padding: 20px;
      background-color: white;
      box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
      border-radius: 8px;
    }
    h1, h2, h3 {
      color: #333;
    }
    h1 {
      text-align: center;
      font-size: 36px;
      margin-bottom: 20px;
    }
    h2 {
      font-size: 28px;
      border-bottom: 2px solid #00aaff;
      padding-bottom: 5px;
      margin-bottom: 15px;
    }
    p {
      font-size: 18px;
      line-height: 1.6;
      color: #666;
    }
    .features-list, .tech-list, .steps-list, .structure-list {
      margin-left: 20px;
    }
    .features-list li, .tech-list li, .steps-list li, .structure-list li {
      margin-bottom: 10px;
    }
    .steps-list {
      counter-reset: step;
    }
    .steps-list li {
      list-style: none;
      margin: 15px 0;
      position: relative;
    }
    .steps-list li:before {
      counter-increment: step;
      content: counter(step);
      position: absolute;
      left: -40px;
      top: 0;
      background: #00aaff;
      color: white;
      width: 30px;
      height: 30px;
      border-radius: 50%;
      display: flex;
      justify-content: center;
      align-items: center;
    }
    .button {
      display: inline-block;
      padding: 10px 20px;
      background-color: #00aaff;
      color: white;
      text-decoration: none;
      border-radius: 4px;
      font-weight: bold;
      transition: background-color 0.3s ease;
    }
    .button:hover {
      background-color: #0088cc;
    }
    .footer {
      text-align: center;
      margin-top: 40px;
      color: #888;
    }
    pre {
      background-color: #f0f0f0;
      padding: 10px;
      border-radius: 4px;
      overflow-x: auto;
    }
  </style>
</head>
<body>
  <div class="container">
    <h1>🎬 DEFTY_MOVIE 🎬</h1>

    <h2>🚀 Project Overview</h2>
    <p><strong>DEFTY_MOVIE</strong> is an innovative project designed to create a movie management system. The project enables users to search, view detailed information, and manage their favorite movie lists effortlessly.</p>

    <h2>🛠️ Technologies Used</h2>
    <ul class="tech-list">
      <li>Java</li>
      <li>Spring Boot</li>
      <li>Maven</li>
      <li>MySQL</li>
    </ul>

    <h2>🔧 Installation & Setup</h2>
    <p>Follow the steps below to set up the project:</p>
    <ol class="steps-list">
      <li>Clone this repository: <pre>git clone https://github.com/NNovember-rain/DEFTY_MOVIE.git</pre></li>
      <li>Navigate to the project directory: <pre>cd DEFTY_MOVIE</pre></li>
      <li>Install necessary dependencies: <pre>mvn clean install</pre></li>
      <li>Configure MySQL database in the <code>application.properties</code> file:</li>
      <pre>
spring.datasource.url=jdbc:mysql://localhost:3306/defty_movie_db
spring.datasource.username=root
spring.datasource.password=yourpassword
      </pre>
      <li>Run the project: <pre>mvn spring-boot:run</pre></li>
    </ol>

    <h2>🌟 Features</h2>
    <ul class="features-list">
      <li>📂 Movie management: Add, delete, and update movie information.</li>
      <li>🔍 Movie search: Quickly search by name or genre.</li>
      <li>📊 View detailed information: Access details such as name, genre, duration, and release date.</li>
      <li>📑 Favorite list: Users can save their favorite movies for easy access.</li>
    </ul>

    <h2>📂 Project Structure</h2>
    <ul class="structure-list">
      <li><strong>.mvn/</strong> - Maven wrapper files</li>
      <li><strong>src/</strong> - Source code</li>
      <li><strong>README.md</strong> - Project description file</li>
      <li><strong>pom.xml</strong> - Maven project configuration</li>
    </ul>

    <h2>👨‍💻 Contributing</h2>
    <p>We welcome contributions from the community. To contribute, please follow these steps:</p>
    <ol class="steps-list">
      <li>Fork the project.</li>
      <li>Create a new branch (<code>git checkout -b feature/AmazingFeature</code>).</li>
      <li>Commit your changes (<code>git commit -m 'Add some AmazingFeature'</code>).</li>
      <li>Push to the branch (<code>git push origin feature/AmazingFeature</code>).</li>
      <li>Open a pull request.</li>
    </ol>

    <h2>📞 Contact</h2>
    <p>If you have any questions about the project, feel free to reach out via email: <strong>nnovember.rain@deftymovie.com</strong>.</p>

    <div class="footer">
      <p>Released under the <a href="https://opensource.org/licenses/MIT" class="button">MIT License</a></p>
    </div>
  </div>
</body>
</html>
