all:
	happy -gca Parjson.y
	alex -g Lexjson.x
#	latex Docjson.tex; dvips Docjson.dvi -o Docjson.ps
	ghc --make Testjson.hs -o Testjson
clean:
	-rm -f *.log *.aux *.hi *.o *.dvi
	-rm -f Docjson.ps
distclean: clean
	-rm -f Docjson.* Lexjson.* Parjson.* Layoutjson.* Skeljson.* Printjson.* Testjson.* Absjson.* Testjson ErrM.* SharedString.* json.dtd XMLjson.* Makefile*

