CREATE TABLE IF NOT EXISTS User_Table (
    userID INTEGER,
    username VARCHAR(20) NOT NULL,
    password VARCHAR(30) NOT NULL,
    mail VARCHAR(50) NOT NULL,
    PRIMARY KEY (userID AUTOINCREMENT),
    UNIQUE(username)
);

CREATE TABLE IF NOT EXISTS Post_Table (
    postID INTEGER,
    userID INTEGER,
    title VARCHAR(100) NOT NULL,
    content VARCHAR(1000) NOT NULL,
    date DATE NOT NULL,
    FOREIGN KEY (userID) REFERENCES User_Table (userID),
    PRIMARY KEY (postID AUTOINCREMENT)
);

CREATE TABLE IF NOT EXISTS Comment_Table (
    commentID INTEGER,
    postID INTEGER,
    author VARCHAR(50) NOT NULL,
    content VARCHAR(1000) NOT NULL,
    date DATE NOT NULL,
    FOREIGN KEY (postID) REFERENCES Post_Table (postID),
    PRIMARY KEY (commentID AUTOINCREMENT)
);