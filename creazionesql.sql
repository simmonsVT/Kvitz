-- Simeone Vitale N86003606
CREATE TABLE insegnante(
username VARCHAR(25) NOT NULL,
password VARCHAR(25) NOT NULL,
nome VARCHAR(25),
cognome VARCHAR(25),
PRIMARY KEY (username)
);

CREATE TABLE studente(
username VARCHAR(25) NOT NULL,
password VARCHAR(25) NOT NULL,
nome VARCHAR(25),
cognome VARCHAR(25),
PRIMARY KEY (username)
);

CREATE TABLE test(
nome VARCHAR(25) PRIMARY KEY,
usernameI VARCHAR(25),
FOREIGN KEY (usernameI) REFERENCES insegnante (username)
);

CREATE TABLE test_svolto(
id SERIAL PRIMARY KEY,
nomeTest VARCHAR(25) NOT NULL,
usernameS VARCHAR(25) NOT NULL,
dataConsegna TIMESTAMP,
FOREIGN KEY (nomeTest) REFERENCES test (nome),
FOREIGN KEY (usernameS) REFERENCES studente (username)
);

CREATE TABLE quiz_multiplo(
id SERIAL PRIMARY KEY,
nomeTest VARCHAR(25) NOT NULL,
domanda VARCHAR(200) NOT NULL,
punteggioEsatto FLOAT NOT NULL,
punteggioErrato FLOAT NOT NULL,
opzione1 VARCHAR(100) NOT NULL,
opzione2 VARCHAR(100) NOT NULL,
opzione3 VARCHAR(100) NOT NULL,
opzione4 VARCHAR(100) NOT NULL,
risposta_esatta INT NOT NULL,
FOREIGN KEY (nomeTest) REFERENCES test (nome)
);

CREATE TABLE quiz_aperto(
id SERIAL PRIMARY KEY,
nomeTest VARCHAR(25) NOT NULL,
domanda VARCHAR(200) NOT NULL,
punteggioMinimo FLOAT NOT NULL,
punteggioMassimo FLOAT NOT NULL,
maxCaratteriRisposta INT NOT NULL,
FOREIGN KEY (nomeTest) REFERENCES test (nome)
);

CREATE TABLE risposta_multiplo(
id SERIAL PRIMARY KEY,
idQuiz INT NOT NULL,
idTestSvolto INT NOT NULL,
risposta INT NOT NULL,
votoAssegnato FLOAT, 
rispostaGiusta BOOLEAN,
FOREIGN KEY (idQuiz) REFERENCES quiz_multiplo (id),
FOREIGN KEY (idTestSvolto) REFERENCES test_svolto (id)
);

CREATE TABLE risposta_aperto(
id SERIAL PRIMARY KEY,
idQuiz INT NOT NULL,
idTestSvolto INT NOT NULL,
risposta VARCHAR(500) NOT NULL, 
votoAssegnato FLOAT, 
rispostaGiusta BOOLEAN,
FOREIGN KEY (idQuiz) REFERENCES quiz_multiplo (id),
FOREIGN KEY (idTestSvolto) REFERENCES test_svolto (id)
);


--vincoli su quiz_multiplo
ALTER TABLE quiz_multiplo
ADD CONSTRAINT check_punteggoMax CHECK ( punteggioEsatto > 0 AND punteggioEsatto > punteggioErrato),
ADD CONSTRAINT check_punteggoMin CHECK ( punteggioErrato <= 0 AND punteggioErrato < punteggioEsatto),
ADD CONSTRAINT check_dominioRisposta CHECK ( risposta_esatta >= 1 AND risposta_esatta <= 4);


--vincoli su quiz_aperto
ALTER TABLE quiz_aperto
ADD CONSTRAINT check_punteggioMax CHECK ( punteggioMassimo > 0 AND punteggioMassimo > punteggioMinimo),
ADD CONSTRAINT check_punteggoMin CHECK ( punteggioMinimo <= 0 AND punteggioMinimo < punteggioMassimo),
ADD CONSTRAINT check_maxCharRisp CHECK ( maxCaratteriRisposta > 0 AND maxCaratteriRisposta <= 500);

--vincoli su risposta_multiplo
ALTER TABLE risposta_multiplo
ADD CONSTRAINT check_dominioRisposta CHECK (risposta >= 1 AND risposta <= 4);




--Funzioni per trigger


--Creazione funzione per check credenziali studente
CREATE OR REPLACE FUNCTION checkCredenzialiS() RETURNS TRIGGER 

LANGUAGE PLPGSQL

AS $checkCredenzialiS$

DECLARE

username studente.username%TYPE;
pwd studente.password%TYPE;
nome studente.nome%TYPE;
cognome studente.cognome%TYPE;

BEGIN

username := NEW.username;
pwd := NEW.password;
nome := NEW.nome;
cognome := NEW.cognome;


IF( LENGTH(username) < 8 OR LENGTH(pwd) < 8) THEN
	IF(LENGTH(username) < 8) THEN
		RAISE EXCEPTION 'Username non valido'
		USING HINT = 'Username deve essere formato da almeno 8 caratteri';
	END IF;
	
	IF(LENGTH(pwd) < 8) THEN
		RAISE EXCEPTION 'Password non valida'
		USING HINT = 'La password deve essere formata da almeno 8 caratteri';
	END IF;
	
END IF;
IF(LENGTH(nome) < 2 OR LENGTH(cognome) < 2) THEN
	RAISE EXCEPTION 'Nome o cognome non valido'
	USING HINT = 'Prova a inserire un nome e cognome vero!';
END IF;
RETURN NEW;
END $checkCredenzialiS$;

CREATE TRIGGER checkCredenziali AFTER INSERT OR UPDATE ON STUDENTE
FOR EACH ROW EXECUTE FUNCTION checkCredenzialiS();


--Creazione funzione per check credenziali insegnante
CREATE OR REPLACE FUNCTION checkCredenzialiI()
RETURNS TRIGGER 
LANGUAGE plpgsql AS $checkCredenzialiI$


DECLARE

username insegnante.username%TYPE;
pwd insegnante.password%TYPE;
nome insegnante.nome%TYPE;
cognome insegnante.cognome%TYPE;

BEGIN

username := NEW.username;
pwd := NEW.password;
nome := NEW.nome;
cognome := NEW.cognome;


IF( LENGTH(username) < 8 OR LENGTH(pwd) < 8) THEN
	IF(LENGTH(username) < 8) THEN
		RAISE EXCEPTION 'Username non valido'
		USING HINT = 'Username deve essere formato da almeno 8 caratteri';
	END IF;
	
	IF(LENGTH(pwd) < 8) THEN
		RAISE EXCEPTION 'Password non valida'
		USING HINT = 'La password deve essere formata da almeno 8 caratteri';
	END IF;
	
END IF;
IF(LENGTH(nome) < 2 OR LENGTH(cognome) < 2) THEN
	RAISE EXCEPTION 'Nome o cognome non valido'
	USING HINT = 'Prova a inserire un nome e cognome vero!';
END IF;
RETURN NEW;
END $checkCredenzialiI$;


CREATE TRIGGER checkCredenziali AFTER INSERT OR UPDATE ON INSEGNANTE
FOR EACH ROW EXECUTE FUNCTION checkCredenzialiI();



-- check username se già registrato
-- vincolo interrelazionale
CREATE OR REPLACE FUNCTION checkUsername() RETURNS TRIGGER LANGUAGE plpgsql AS $checkCredenziali$

DECLARE

username VARCHAR(25);
contAccountS INTEGER;
contAccountI INTEGER;
BEGIN


SELECT COUNT(*) INTO contAccountI
FROM insegnante
WHERE insegnante.username = NEW.username;

IF contAccountI > 0 THEN
	RAISE EXCEPTION 'Esiste già un insegnante associato a questo username!'
	USING HINT = 'Si prega di utilizzare un altro username';
END IF;

SELECT COUNT(*) INTO contAccountS
FROM studente
WHERE studente.username = NEW.username;

IF contAccountS > 0 THEN
	RAISE EXCEPTION 'Esiste già uno studente associato a questo username!'
	USING HINT = 'Si prega di utilizzare un altro username';
END IF;

RETURN NEW;

END $checkCredenziali$;




CREATE TRIGGER check_userName BEFORE INSERT OR UPDATE ON studente
FOR EACH ROW EXECUTE FUNCTION checkusername();
CREATE TRIGGER check_userName BEFORE INSERT OR UPDATE ON insegnante
FOR EACH ROW EXECUTE FUNCTION checkusername();



-- verifica numero caratteri risposta quiz aperto
-- vincolo interrelazionale
CREATE FUNCTION checkRispostaAperta() RETURNS TRIGGER LANGUAGE plpgsql AS $checkRispostaAperta$
DECLARE
maxCharRisp quiz_aperto.maxcaratteririsposta%TYPE;
BEGIN

SELECT maxcaratteririsposta INTO maxCharRisp
FROM quiz_aperto
WHERE id = NEW.idQuiz;

IF (LENGTH(NEW.risposta) > maxCharRisp) THEN
	RAISE EXCEPTION 'La risposta inserita supera il limite di % caratteri',maxCharRisp
	USING HINT = 'Riprova inserendo una risposta più breve';
END IF;
RETURN NEW;
END $checkRispostaAperta$;

CREATE TRIGGER checkRisposta AFTER INSERT OR UPDATE ON risposta_aperto
FOR EACH ROW EXECUTE FUNCTION checkRispostaAperta();


-- check voto assegnato risposta multipla
-- vincolo interrelazionale
CREATE FUNCTION check_votoAssegnato() RETURNS TRIGGER LANGUAGE plpgsql AS $check_votoAssegnato$

DECLARE 
votoMin quiz_aperto.punteggioMinimo%TYPE;
votoMax quiz_aperto.punteggioMassimo%TYPE;

BEGIN

SELECT punteggioMinimo,punteggioMassimo INTO votoMin,votoMax
FROM quiz_aperto
WHERE id = NEW.idQuiz;

IF NEW.votoAssegnato > votoMax OR NEW.votoAssegnato < votoMin THEN
	RAISE EXCEPTION 'Il punteggio inserito non rispetta il range inserito in fase di creazione del quiz (%<=x<=%)',votoMin,votoMax
	USING HINT = 'Riprova inserendo un punteggio conforme';
END IF;
RETURN NEW;
END $check_votoAssegnato$;

CREATE TRIGGER check_votoAssegnato AFTER INSERT OR UPDATE ON risposta_aperto
FOR EACH ROW EXECUTE FUNCTION check_votoAssegnato();



CREATE FUNCTION check_numQuiz() RETURNS TRIGGER LANGUAGE plpgsql AS $check_numQuiz$
DECLARE
numQuizMultiplo INTEGER;
numQuizAperto INTEGER;
totQuiz INTEGER;
BEGIN
SELECT COUNT(*) INTO numQuizMultiplo
FROM quiz_multiplo 
WHERE nomeTest = NEW.nomeTest;

IF (numQuizMultiplo >= 5) THEN
	RAISE EXCEPTION 'Sono presenti già 5 quiz a risposta multipla, impossibile inserire ulteriori quiz';

END IF;

SELECT COUNT(*) INTO numQuizAperto
FROM quiz_aperto 
WHERE nomeTest = NEW.nomeTest;

IF (numQuizAperto >= 5) THEN
	RAISE EXCEPTION 'Sono presenti già 5 quiz a risposta aperta, impossibile inserire ulteriori quiz';

END IF;

totQuiz := numQuizMultiplo + numQuizAperto;

IF (totQuiz >= 5) THEN
	RAISE EXCEPTION 'Sono presenti già 5 quiz, impossibile inserire ulteriori quiz';
END IF; 
RETURN NEW;
END $check_numQuiz$;

CREATE TRIGGER check_numQuiz BEFORE INSERT OR UPDATE ON quiz_aperto
FOR EACH ROW EXECUTE FUNCTION check_numQuiz();

CREATE TRIGGER check_numQuiz BEFORE INSERT OR UPDATE ON quiz_multiplo
FOR EACH ROW EXECUTE FUNCTION check_numQuiz();







--Automazioni

-- correzione risposta multipla
CREATE FUNCTION correggi_quizMultiplo() RETURNS TRIGGER LANGUAGE plpgsql AS $correggi_quizMultiplo$

DECLARE 
votoGiusta quiz_multiplo.punteggioEsatto%TYPE;
votoErrato quiz_multiplo.punteggioErrato%TYPE;
indiceRispostaGiusta quiz_multiplo.risposta_esatta%TYPE;
BEGIN

SELECT punteggioEsatto,punteggioErrato,risposta_esatta INTO votoGiusta,votoErrato,indiceRispostaGiusta
FROM quiz_multiplo
WHERE id = NEW.idQuiz;

IF NEW.risposta = indiceRispostaGiusta THEN
	NEW.votoAssegnato := votoGiusta;
	NEW.rispostaGiusta := TRUE;
ELSE
	NEW.votoAssegnato := votoErrato;
	NEW.rispostaGiusta := FALSE;
END IF;
RETURN NEW;
END $correggi_quizMultiplo$;

CREATE TRIGGER correggi_quizMultiplo BEFORE INSERT OR UPDATE ON risposta_multiplo
FOR EACH ROW EXECUTE FUNCTION correggi_quizMultiplo();


-- imposta dataConsegna all'inserimento del record prendendolo dal DBMS
CREATE FUNCTION check_dataConsegna() RETURNS TRIGGER LANGUAGE plpgsql AS $check_dataConsegna$

BEGIN

NEW.dataConsegna := current_timestamp(0);

RETURN NEW;

END $check_dataConsegna$;



CREATE TRIGGER assegnaDataConsegna  BEFORE INSERT ON test_svolto
FOR EACH ROW EXECUTE FUNCTION check_dataConsegna();



--VIEW

 


--visualizza tutte le risposte date ai quiz multipli

CREATE VIEW get_risposteQuizMultipliCorretti AS (
	SELECT ts.nometest,q.domanda,r_m.votoAssegnato,s.nome,s.cognome
	FROM
	risposta_multiplo as r_m,quiz_multiplo as q,test as t,test_svolto as ts,studente as s
	WHERE r_m.idQuiz = q.id AND q.nomeTest = t.nome  AND r_m.idtestsvolto = ts.id AND ts.usernameS = s.username AND r_m.votoAssegnato = q.punteggioesatto  
);


CREATE VIEW get_risposteQuizApertiCorretti AS (
 SELECT test_svolto.nomeTest, 
	risposta_aperto.id,
    risposta_aperto.idquiz,
    risposta_aperto.idtestsvolto,
    risposta_aperto.risposta,
    risposta_aperto.votoassegnato,
    risposta_aperto.rispostagiusta
   FROM risposta_aperto,test_svolto
  WHERE risposta_aperto.rispostagiusta = true AND test_svolto.id = risposta_aperto.idtestsvolto

);


-- visualizza il numero di test svolti per ogni test

CREATE VIEW get_contTestSvolti AS(

	SELECT test.nome,COUNT(*) as Test_Svolti 
	FROM test_svolto,test
	WHERE test_svolto.nomeTest = test.nome
	GROUP BY test.nome
);

-- numero di test svolti per ogni studente

CREATE VIEW get_countTestSvoltiPerStudente AS(

	SELECT s.username,s.nome,s.cognome,COUNT(*) as numero_test_svolti
	FROM studente as s,test_svolto as ts,test as t
	WHERE s.username = ts.usernameS AND t.nome = ts.nomeTest
	GROUP BY s.username

);

--INSERT

INSERT INTO studente (nome,cognome,username,password) VALUES ('Simeone','Vitale','simmons_vt','pippo1234');
INSERT INTO studente (nome,cognome,username,password) VALUES ('Pasquale','Franchini','pakoFran','password');
INSERT INTO studente (nome,cognome,username,password) VALUES ('Silvio','Puca','silvio01','silviopellico');
INSERT INTO studente (nome,cognome,username,password) VALUES ('Salvatore','Gervasio','ichigo01','bleach4life');
INSERT INTO studente (nome,cognome,username,password) VALUES ('Carmine','Casillo','3i4everr','acapulco1512');

INSERT INTO insegnante (nome,cognome,username,password) VALUES ('Silvio','Barra','silvio85','basidati');
INSERT INTO insegnante (nome,cognome,username,password) VALUES ('Ennio','Ranucci','ennio467','casertalove');
INSERT INTO insegnante (nome,cognome,username,password) VALUES ('Antonio','Di Giacomo','dagcsm59','micronst');

INSERT INTO test (nome,usernameI) VALUES ('esame070923','silvio85');
INSERT INTO test (nome,usernameI) VALUES ('esame0623','silvio85');
INSERT INTO test (nome,usernameI) VALUES ('elettronica','dagcsm59');
INSERT INTO test (nome,usernameI) VALUES ('java','ennio467');


INSERT INTO quiz_aperto (nomeTest,domanda,punteggioMinimo,punteggioMassimo,maxCaratteriRisposta) VALUES 
								('esame070923','IN COSA CONSISTEVA IL PIANO "LEONE MARINO"?',0,10,500),
								('esame070923','DESCRIVI A PAROLE TUE LA VITA E LA POETICA DI G.UNGARETTI',-1,5,300),
								('esame070923','QUALE FU IL PRIMO EASTER EGG DELLA STORIA?',-2.5,4.25,499),
								('esame070923','COME FU ASSASSINATO GIULIO CESARE?',0,8.67,250),
								('esame070923', 'COSA STUDIA LA OPTOMETRIA?',0,6,100),
								('elettronica', 'DESCRIVI LA DIFFERENZA TRA DRAM E SRAM',0,6,475),
								('elettronica', 'SCRIVI LA DEFINIZIONE DI "CORRENTE ELETTRICA"',0,6,100),
								('java', 'COSA SI INTENDE PER POLIMORFISMO?',0,6,80);
								
INSERT INTO quiz_multiplo (nomeTest,domanda,punteggioErrato,punteggioEsatto,opzione1,opzione2,opzione3,opzione4,risposta_esatta) VALUES 
								('esame0623','QUANTO FA 1 + 1?',-0.25,1,'2','1','4','5',1),
								('esame0623','QUALE DI QUESTE CONSOLE È STATA CREATA DA SONY?',-0.50,1.25,'XBOX ONE','NINTENDO64','PSONE','SEGA SATURN',3),
								('esame0623','SCEGLI IL NOME DEL PROF. DI BASI DI DATI',-1,10,'PIER SILVIO','SILVIO','SALVO','SAVERIO',2),
								('esame0623','CHI TRA QUESTI MATEMATICI HA DECIFRATO IL CODICE ENIGMA?',-0.25,1,'BLAISE PASCAL','JOHN VON NEUMANN','RENATO CACCIOPPOLI','ALAN TURING',4),
								('esame0623','CHI ERA IL PRIMO MINISTRO ITALIANO NEL 2020?',-1.55,2,'GIORGIA MELONI','GIUSEPPE CONTE','SILVIO BERLUSCONI','BENITO MUSSOLINI',2),
								('elettronica','A COSA EQUIVALE LA TENSIONE (V) SECONDO LE LEGGI DI OHM?',-0.25,1,'V = I*R','V = I/R','V = R/I','V = R + I',1),
								('java','A COSA SERVE IL SIMBOLO = NEL LINGUAGGIO JAVA?',-0.50,1,'SOTTRAZIONE','ASSEGNAZIONE','CANCELLAZIONE','DIVISIONE',2),
								('java','A QUANTO EQUIVALE ',-0.50,1,'SOTTRAZIONE','ASSEGNAZIONE','CANCELLAZIONE','DIVISIONE',2);


INSERT INTO test_svolto (nomeTest,usernameS) VALUES
								('elettronica','simmons_vt'),
								('elettronica','pakoFran'),
								('java','simmons_vt'),
								('java','ichigo01'),
								('esame070923','simmons_vt'),
								('esame0623','simmons_vt');
			
-- ATTENZIONE!	idTestSvolto va sostituito ai valori assegnati in base ai valori assunti nel db visto che è SERIAL!!
INSERT INTO risposta_multiplo (idTestSvolto,idquiz,risposta) VALUES 
								(6,1,1),
								(6,2,4),
								(6,3,2),
								(6,4,3),
								(6,5,2),
								(1,6,1),
								(2,6,3);
								
-- ATTENZIONE!	idTestSvolto va sostituito ai valori assegnati in base ai valori assunti nel db visto che è SERIAL!!
-- include anche la correzione!					
INSERT INTO risposta_aperto (idTestSvolto,idquiz,risposta,votoAssegnato,rispostagiusta) VALUES 
								(5,1,'attacco della germania a regno unito durante WW2',6,true),
								(5,2,'massimo esponente letteratura italiana del 900',4,true),
								(5,3,'viene stampato a video nome programmatore nel videogioco Adventure, 1980',3.75,true),
								(5,4,'pugnalato',0,false),
								(5,5,'misurazione e miglioramento della efficienza visiva',6,true),
								(1,6,'una è costituita da flip flop e un altra da condensatori',6,true),
								(1,7,'rapporto tra tensione e resistenza',5.56,true),
								(2,6,'sono memorie volatili una piu veloce e un altra piu lenta',3,true),
								(2,7,'sono entrambe unita di misura',1,true),
								(3,8,'possibilità di una espressione di esprimere diversi significati',4,true),
								(4,8,'assegnare valore a una variabile',0,false);
							
							
-- Simeone Vitale N86003606