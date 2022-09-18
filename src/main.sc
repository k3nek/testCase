require: slotfilling/slotFilling.sc
  module = sys.zb-common
require: patterns.sc
require: forgetPass.sc

theme: /

    state: Start
        q!: $regex</start>
        q!: $hello
        a: Здравствуйте!

    state: Bye
        q!: * $bye *
        q!: * {всего * (~добрый|~хороший)} *
        a: Приятно было пообщаться. Всегда готов помочь вам снова 🙂

    state: NoMatch
        event!: noMatch
        a: Извините, этого ещё не знаю.

    state: Match
        event!: match
        a: {{$context.intent.answer}}
        