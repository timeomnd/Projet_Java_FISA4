-- Insert a new user into the registration form
INSERT INTO registration_form (first_name, last_name, email)
VALUES ('John', 'Doe', 'john.doe@example.com');

-- Insert credentials into the authentication table (linked to user id 1)
INSERT INTO authentication (username, password_hash, id_registration_form)
VALUES ('johndoe99', 'hashed_password_example', 1);

-- Insert games with their respective cover paths from the resources folder
INSERT INTO games (title, cover) VALUES
                                     ('Elden Ring', 'resources/covers/elden_ring.jpg'),
                                     ('God of War', 'resources/covers/god_of_war.jpg'),
                                     ('The Last of Us Part I', 'resources/covers/the_last_of_us_part_i.jpg'),
                                     ('Cyberpunk 2077', 'resources/covers/cyberpunk_2077.jpg'),
                                     ('Naruto Shippuden: Ultimate Ninja Storm 2', 'resources/covers/naruto_storm_2.jpg');

-- Insert the user's relationship with these games
INSERT INTO user_games (played, platinum, `100_percent`, wishlist_games, wishlist_platinum, game_rating, platinum_difficulty, time_played_hours, id_authentication, id_games)
VALUES
-- User (id 1) has played Elden Ring (id 1) and got the platinum trophy
(1, 1, 1, 0, 0, 10, 9, 135.50, 1, 1),

-- User (id 1) is currently playing God of War (id 2) and wants the platinum
(1, 0, 0, 0, 1, 9, 6, 42.00, 1, 2),

-- Cyberpunk 2077 (id 4) is on the user's wishlist
(0, 0, 0, 1, 0, NULL, NULL, 0.00, 1, 4);