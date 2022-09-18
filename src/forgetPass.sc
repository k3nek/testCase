require: slotfilling/slotFilling.sc
  module = sys.zb-common
require: patterns.sc
require: main.sc

theme: /forgetPass
    
    state: forgetPassSpecify
        q!: * {($forget/$change/$restore/$reset/потеря*/утеря*/вспомн*/напомн*/забрал*) * $password} *
        q!: * {$cannot * ($change/$restore/$reset/вспомн*/напомн*/установить/задать/поставить) * $password} *
        a: Сейчас расскажу порядок действий.
            Выберите, что именно планируете сделать:
            1. Поменять пароль для входа в приложение.
            2. Поменять PIN-код от карты.
            Пожалуйста, отправьте цифру, соответствующую вашему выбору.
            
        state: forgetPassApp
            q: * $1 *
            q!: * {($forget/$change/$restore/$reset/потеря*/утеря*/вспомн*/напомн*) * $password * ($app/$lk/вход*/доступ*)} *
            q!: * {{$cannot * ($change/$restore/$reset/вспомн*/напомн*/отправ*/направ*/установить/задать/поставить)} * $password * ($app/$lk/вход*/доступ*)} *
            q!: * {($forget/$change/$restore/$reset/потеря*/утеря*/вспомн*/напомн*) * $password * логин*} *
            script:
                $response.replies = $response.replies || [];
                $response.replies.push({
                  "type":"timeout",
                  "interval":2,
                  "targetState":"/forgetPass/forgetPassSpecify/forgetPassApp/forgetPassAppAnsEnd"
                });
            a: Смена пароля от приложения возможна несколькими способами:
                1. на экране "Профиль" выберите "Изменить код входа в приложение".
                2. введите SMS-код.
                3. придумайте новый код для входа в приложение и повторите его.
            
            state: forgetPassAppAnsEnd
                script:
                    $response.replies = $response.replies || [];
                    $response.replies.push({
                      "type":"timeout",
                      "interval":2,
                      "targetState":"/Bye"
                    });
                a: Либо нажмите на кнопку "Выйти" на странице ввода пароля для входа в приложение.
                    После чего нужно будет заново пройти регистрацию:
                    1. ввести полный номер карты (если оформляли ранее, иначе номер телефона и дату рождения),
                    2. указать код из смс-код,
                    3. придумать новый пароль для входа.
                
        state: forgetPassCard
            q: * $2 *
            q!: * {($forget/$change/$restore/$reset/потеря*/утеря*/вспомн*/напомн*) * $password * карт* [$app/$lk]} *
            q!: * {{$cannot * ($change/$restore/$reset/вспомн*/напомн*)} * $password * карт*} *
            q!: * {(сделать/установить/задать/поставить) * ((~новый/~новенький) $password) * карт*} *
            q!: * {(не (подходит/проходит)) * {~старый * $password * карт*}} *
            a: Это можно сделать в приложении:
                1. На экране "Мои деньги" в разделе "Карты" нажмите на нужную.
                2. Выберите вкладку "Настройки".
                3. Нажмите "Сменить пин-код".
                4. И введите комбинацию, удобную вам.
                5. Повторите ее.
            go!: /forgetPass/forgetPassSpecify/forgetPassCard/forgetPassCardAnsEnd
                
            state: forgetPassCardAnsEnd
                a: И все готово!
                    Пин-код установлен, можно пользоваться. 🙂
                go!: /Bye
