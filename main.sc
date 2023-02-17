require: hangmanGameData.csv
    name = HangmanGameData
    var = $HangmanGameData

require: functions.js

patterns:
    $Word = $entity<HangmanGameData> || converter = function ($parseTree) {
        var id = $parseTree.HangmanGameData[0].value;
        return $HangmanGameData[id].value;
        };
        

theme: /

    state: Start 
        q!: $regex</start>
        intent!: /Hello
        a: Привет, давай играть в виселицу! Я загадаю слово, а ты угадывай по буквам или называй слово целиком. Го?

        state: Agree
            intent: /Agree
            go!: /Play

        state: Disagree
            intent: /Disagree
            a: Если что, пиши "играть" ;)


    state: Play
        intent!: /LetsPlay
        script:
            # количество слов в файле hangmanGameData.csv
            var number = Object.keys($HangmanGameData).length;
            # случайное слово из файла hangmanGameData.csv
            $session.hidden = $HangmanGameData[$jsapi.random(number)].alternateNames[0];
            # разметка слова подчеркиваниями (функция repeat() не работает ¯\_(ツ)_/¯ )
            $session.marking = '';
            for (var i=0; i < $session.hidden.length; i++){
                $session.marking += '_ ';
            }
            # количество ошибок
            $session.wrong = 0;
            $reactions.answer('Я загадал, {{$session.marking.length/2}} букв, угадывай!');
            $reactions.answer(" {{ $session.marking }} ");
            # $reactions.answer($session.hidden) // вывод в чат загаданного слова

        
        state: CheckLetter
            intent: /Letter
            script:
                var define = checkLetter($parseTree._Letter.toLowerCase(), $session.hidden, $session.marking, $session.wrong);
                if (typeof define == 'string'){ // если угадана буква
                    $session.marking = define;
                    $reactions.answer(" {{ $session.marking }} ");
                }
                if (typeof define == 'number'){ // если ошибка
                    $session.wrong = define;
                }

        
        state: CheckWord
            intent: /Word
            script:
                var define = checkWord($parseTree._Wordd.toLowerCase(), $session.hidden, $session.wrong);
                if (typeof define == 'number'){ // если ошибка
                    $session.wrong = define;
                }
        
        
        state: Unfit
            intent: /Unfit
            a: Только русские буквы
        
        
        state: LocalCatchAll
            event: noMatch
            random:
                a: Ты что-то не то вводишь..
                a: Нужно вводить букву или слово
    
    
    state: NoMatch || noContext = true
        event: noMatch
        random:
            a: Я не понимаю
            a: Что вы имеете в виду?
    
    
    state: EndGame
        intent!: /EndGame
        a: Игра закончилась. Для новой игры пиши "играть"
        
    
    state: Bye
        intent!: /Bye
        a: Пока!
            


