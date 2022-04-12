INSERT INTO gift_certificate (name, description, price, duration) VALUES ('First tandem skydive', 'A Tandem skydive adventure is the free-falling thrill of a lifetime! Almost anyone 18 years of age or older can enjoy a tandem skydive–it’s great for all the adults on your list! Our Tandem Skydive Gift Certificate can be used any day of the week.', 388, 30);
INSERT INTO gift_certificate (name, description, price, duration) VALUES ('Delta planing', 'Delta planing is an exfoliating procedure using a straight-edge blade. The treatment will allow products and laser therapies to penetrate the skin more easily', 150, 60);
INSERT INTO gift_certificate (name, description, price, duration) VALUES ('GO-KARTING', 'Take the gamble out of gift-giving. Give your champion an Ace Karts gift card.', 30, 365);

INSERT INTO tag (name) VALUES ('#speed');
INSERT INTO tag (name) VALUES ('#extreme');
INSERT INTO tag (name) VALUES ('#beauty');
INSERT INTO tag (name) VALUES ('#rest');

INSERT INTO certificates_tags (certificate_id, tag_id) VALUES (1, 1);
INSERT INTO certificates_tags (certificate_id, tag_id) VALUES (1, 2);
INSERT INTO certificates_tags (certificate_id, tag_id) VALUES (2, 3);
INSERT INTO certificates_tags (certificate_id, tag_id) VALUES (2, 4);
INSERT INTO certificates_tags (certificate_id, tag_id) VALUES (3, 1);