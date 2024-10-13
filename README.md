# 🎬 DEFTY_MOVIE 🎬

## 🚀 Project Overview
**DEFTY_MOVIE** is an innovative project designed to create a movie management system. The project enables users to search, view detailed information, and manage their favorite movie lists effortlessly.

## 🛠️ Technologies Used
- Java
- Spring Boot
- Maven
- MySQL

## 🔧 Installation & Setup
Follow the steps below to set up the project:

1. Clone this repository:
    ```bash
    git clone https://github.com/NNovember-rain/DEFTY_MOVIE.git
    ```

2. Navigate to the project directory:
    ```bash
    cd DEFTY_MOVIE
    ```

3. Install necessary dependencies:
    ```bash
    mvn clean install
    ```

4. Configure MySQL database in the `application.yaml` file:
    ```yaml
    url: jdbc:mysql://movie.cdo2go0s62es.ap-southeast-2.rds.amazonaws.com:3306/movie
    username: admin
    password: 12345678
    ```

5. Run the project:
    ```bash
    mvn spring-boot:run
    ```

## 🌟 Features
- 📂 **Movie management**: Add, delete, and update movie information.
- 🔍 **Movie search**: Quickly search by name or genre.
- 📊 **View detailed information**: Access details such as name, genre, duration, and release date.
- 📑 **Favorite list**: Users can save their favorite movies for easy access.

## 📂 Project Structure
- **.mvn/** - Maven wrapper files
- **src/** - Source code
- **README.md** - Project description file
- **pom.xml** - Maven project configuration

## 👨‍💻 Contributing
We welcome contributions from the community. To contribute, please follow these steps:

1. Fork the project.
2. Create a new branch:
    ```bash
    git checkout -b feature/AmazingFeature
    ```
3. Commit your changes:
    ```bash
    git commit -m 'Add some AmazingFeature'
    ```
4. Push to the branch:
    ```bash
    git push origin feature/AmazingFeature
    ```
5. Open a pull request.

## 📞 Contact
If you have any questions about the project, feel free to reach out via email: **nnovember.rain@deftymovie.com**.

---

Released under the [MIT License](https://opensource.org/licenses/MIT).
