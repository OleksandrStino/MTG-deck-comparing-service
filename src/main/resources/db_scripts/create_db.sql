CREATE TABLE "app_user" (
	"username" varchar(60) NOT NULL UNIQUE,
	"enabled" bool NOT NULL DEFAULT '1',
	"password" varchar(60) NOT NULL,
	"role" varchar(20) NOT NULL,
	CONSTRAINT app_user_pk PRIMARY KEY ("username")
) WITH (
  OIDS=FALSE
);



CREATE TABLE "card" (
	"multiverseid" bigint NOT NULL,
	"name" varchar(60) NOT NULL,
	"image" TEXT NOT NULL,
	"type" varchar(20) NOT NULL,
	"rarity" varchar(15) NOT NULL,
	"set" varchar(20) NOT NULL,
	"text" TEXT NOT NULL,
	CONSTRAINT card_pk PRIMARY KEY ("multiverseid")
) WITH (
  OIDS=FALSE
);



CREATE TABLE "deck" (
	"id" serial NOT NULL,
	"name" varchar(18) NOT NULL,
	"user_id" varchar(60) NOT NULL,
	CONSTRAINT deck_pk PRIMARY KEY ("id")
) WITH (
  OIDS=FALSE
);



CREATE TABLE "card_deck" (
	"card_id" bigint NOT NULL,
	"deck_id" bigint NOT NULL,
	"count" int NOT NULL
) WITH (
  OIDS=FALSE
);





ALTER TABLE "deck" ADD CONSTRAINT "deck_fk0" FOREIGN KEY ("user_id") REFERENCES "app_user"("username");

ALTER TABLE "card_deck" ADD CONSTRAINT "card_deck_fk0" FOREIGN KEY ("card_id") REFERENCES "card"("multiverseid");
ALTER TABLE "card_deck" ADD CONSTRAINT "card_deck_fk1" FOREIGN KEY ("deck_id") REFERENCES "deck"("id");
