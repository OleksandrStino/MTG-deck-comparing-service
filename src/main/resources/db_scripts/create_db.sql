CREATE TABLE "app_user" (
  "id" serial NOT NULL,
  "name" varchar(60) NOT NULL UNIQUE,
  CONSTRAINT app_user_pk PRIMARY KEY ("id")
) WITH (
OIDS=FALSE
);



CREATE TABLE "card" (
  "multiverseid" bigint NOT NULL,
  "name" varchar(60) NOT NULL,
  CONSTRAINT card_pk PRIMARY KEY ("multiverseid")
) WITH (
OIDS=FALSE
);



CREATE TABLE "deck" (
  "id" serial NOT NULL,
  "name" varchar(18) NOT NULL,
  "user_id" bigint NOT NULL,
  CONSTRAINT deck_pk PRIMARY KEY ("id")
) WITH (
OIDS=FALSE
);



CREATE TABLE "card_deck" (
  "card_id" bigint NOT NULL,
  "deck_id" bigint NOT NULL
) WITH (
OIDS=FALSE
);





ALTER TABLE "deck" ADD CONSTRAINT "deck_fk0" FOREIGN KEY ("user_id") REFERENCES "app_user"("id");

ALTER TABLE "card_deck" ADD CONSTRAINT "card_deck_fk0" FOREIGN KEY ("card_id") REFERENCES "card"("multiverseid");
ALTER TABLE "card_deck" ADD CONSTRAINT "card_deck_fk1" FOREIGN KEY ("deck_id") REFERENCES "deck"("id");
