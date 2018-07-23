ALTER TABLE pages ADD COLUMN lang character varying(255);
UPDATE pages SET lang = 'PL';

INSERT INTO pages (dtype,version,lang, content) VALUES('HomePage',0,'EN','<blockquote style="margin: 0px 0px 0px 40px; border: none; padding: 0px;"><div style="border-bottom-width: 1px; border-bottom-style: solid; border-bottom-color: rgb(25, 125, 225); padding-bottom: 10px;"><font size="5" color="#197DE1"><b>About project</b></font></div><div><br></div><div><font size="4">ChronoPress Corpus and Search engine  were created as part of the work of the scientific consortium "CLARIN - Polish Common Language Resources and Technology Infrastructure". CLARIN-PL is a part of the "Polish Road Map of Research Infrastructure" and the "European Roadmap for Research Infrastructures, European Strategy Forum on Research Infrastructures". The task was financed from the Ministry of Science and Higher Education funds.</font></div></blockquote><div><br></div><blockquote style="margin: 0px 0px 0px 40px; border: none; padding: 0px;"><div><font size="4" color="#197DE1"><b>Tools</b></font></div><div>ChronoPress uses the Polish language lematizer (Tager WCRFT2 with morphological analyzer Morfeusz 2&nbsp;) to generate morphosyntactic corpus "cross-sections", i. e. sub-sets consisting of specific parts of speech. In addition, the user can also use the tool of automatic naming unit recognition (Liner), as well as some semantic classes (based on Słowosieć (Polish wordnet) and dedicated lexical resources). From the users point of view, a specific feature of the ChronoPress corpus is the implementation of tools for quantitative analysis of text (generating turnout lists, calculating quantitative parameters) and time series, i. e. graphs of lexical parameters over time.</div></blockquote><div><blockquote style="margin: 0px 0px 0px 40px; border: none; padding: 0px;"><div><br></div><div><b><font size="4" color="#197DE1">Corpus</font></b></div><div>The ChronoPress corpus contains approx. 56000 carefully selected excerpts of press texts (used on the basis of quotation rules), prepared linguistically at the morphosyntactic and chronologically structured level. The samples are approximately approx. 300 text words and represent the full thematic spectrum of the official public discourse in 1945-1954 (average of 12 different titles of newspapers or periodicals per year). The vast majority of the texts were obtained from the resources of Wroclaw libraries: the Library of the University of Wroclaw and the Wroclaw National Institute. Ossolinski. Digital versions of some magazines were also used, made available by the Digital Libraries Federation. In order to obtain material from 1945, the Library of the University of Warsaw, the Central Military Library of the Fryderyk Chopin University of Warsaw, and the Library of the Polish Army, Warsaw University, helped to find texts. The Museum of the History of Polish Jews was founded by Professor Józef Pi?sudski and the University Library of St. Joseph Pi?sudski. Jerzy Giedroyc in Białystok.</div><div><br></div></blockquote></div><div><blockquote style="margin: 0px 0px 0px 40px; border: none; padding: 0px;"><div><b><font size="4" color="#197DE1">Team</font></b></div><div>The concept creator and coordinator of the work on the ChronoPress corps is Prof. Dr. hab. Adam Pawłowski from the University of Wroclaw (Institute for Scientific Information and Library Science). The process of acquisition of text material was carried out by a group of approx. 60 people - students, doctoral students and employees of the University of Wroclaw. Programming works in the testing phase of the project were carried out by the CLARIN consortium collaborators from the University of?ód?, the final version of the portal and search engine were prepared by the employees of the Wroclaw University of Technology.</div></blockquote></div>');
