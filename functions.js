function checkLetter(letter, hidden, marking, wrong){
    if (hidden.indexOf(letter) != -1){ // если есть такая буква
        return replace(letter, hidden, marking);
    }
    else { // если нет такой буквы
        $reactions.answer('Нет буквы "' + letter + '"');
        wrong ++;
        return countWrong(wrong);
    }
}


function checkWord(word, hidden, wrong){
    if (word == hidden){
        $reactions.answer('Ты угадал слово!');
        $reactions.transition("/EndGame");
    }
    else {
        $reactions.answer('Неправильное слово');
        wrong ++;
        return countWrong(wrong);
    }
}


function replace(letter, hidden, marking){
    /* функция заменяет нижнее подчеркивание на угаданную букву 
        возвращает разметку слова с угаданной буквой 
        или завершает игру если угаданы все буквы */
    for (var i=0; i < hidden.length; i++){
        if (hidden[i] == letter){
            marking = marking.slice(0, i*2) + letter + ' ' + marking.slice(i*2+2, marking.length);
        }
    }
    if (marking.indexOf('_') == -1){ // если угаданы все буквы
        $reactions.answer('Ты угадал слово!');
        $reactions.transition("/EndGame");
        return;
    }
    return marking;
}


function countWrong(wrong){
    /* функция возвращает кол-во ошибок 
        или завершает игру если сделано 6 ошибок */
    /* противоречие в задании: "игроку дается право на 6 ошибок.." (то есть можно сделать 6 ошибок)
        но при этом "игра завершается, если .. игрок совершил 6 ошибок" */
    switch(wrong) {
        case 1:
        case 2:
        case 3:
            $reactions.answer(" {{ $session.marking }} ");
            return wrong;
            break;
        case 4:
            $reactions.answer(" {{ $session.marking }} \n Осталось две жизни!");
            return wrong;
            break;
        case 5:
            $reactions.answer(" {{ $session.marking }} \n Еще одна ошибка и всё..");
            return wrong;
            break;
        default:
            $reactions.answer("Это было слово {{ $session.hidden }} ");
            $reactions.transition("/EndGame");
    }
}

