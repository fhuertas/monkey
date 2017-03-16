# Monkey Problem

[![Build Status](https://travis-ci.org/fhuertas/monkey.svg?branch=master)](https://travis-ci.org/fhuertas/monkey)
[![Coverage Status](https://coveralls.io/repos/github/fhuertas/monkey/badge.svg?branch=feature%2Fstructure)](https://coveralls.io/github/fhuertas/monkey?branch=feature%2Fstructure)

## English description

We want to know if African monkeys can be taught about deadlocks. She locates a deep canyon and
fastens a rope across it, so the monkeys can cross hand-over-hand.

Passage along the rope follows these rules:

- Several monkeys can cross at the same time, provided that they are all going in the same
direction.
- If eastward moving and westward moving monkeys ever get onto the rope at the same time, a
deadlock will result (the monkeys will get stuck in the middle) because it is impossible for one
monkey to climb over another one while suspended over the canyon.
- If a monkey wants to cross the canyon, he must check to see that no other monkey is currently
crossing in the opposite direction.
- Your solution should avoid starvation. When a monkey that wants to cross to the east arrives at
the rope and finds monkeys crossing to the west, the monkey waits until the rope in empty, but
no more westward moving monkeys are allowed to start until at least one monkey has crossed
the other way.

For this exercise, you are to write a program to simulate activity for this canyon crossing problem:

- Simulate each monkey as a separate process.
- Altogether, a lot of monkeys, whatever you want, will cross the canyon, with a random number
generator specifying whether they are eastward moving or westward moving.
- Use a random number generator, so the time between monkey arrivals is between 1 and 8
seconds.
- Each monkey takes 1 second to get on the rope. (That is, the minimum inter-monkey spacing is
1 second.)
- All monkeys travel at the same speed. Each traversal takes exactly 4 seconds, after the monkey
is on the rope.

Enjoy it!

## Spanish description

Queremos saber si los monos africanos pueden ser enseñados sobre los deadlocks. Hay un cañón profundo y una cuerda que lo atraviesa (Este - Oeste), por esta cuerda los monios pueden cruzar utilizando sus manos

La reglas para poder cruzar son las siguintes:

- Varios monos pueden cruzar al mismo tiempo, siempre y cuando todos vayan en la misma dirección.
- Si los monos que se mueven hacia el este y hacia el oeste llegan a la cuerda al mismo tiempo, se producirá un bloqueo (los monos quedarán atascados en el medio) porque es imposible que un mono monte sobre otro mientras está suspendido sobre el cañón.
- Si un mono quiere cruzar el cañón, debe comprobar para ver que ningún otro mono está cruzando actualmente en la dirección opuesta.
- Su solución debe evitar el hambre. Cuando un mono que quiera cruzar al este llega a la cuerda, si encuentra monos que cruzan al oeste, el mono espera hasta que la cuerda esté vacía, no se permite que crucen más monos hacia el este hasta que al menos uno haya cruzado en el otro sentido.

Para este ejercicio, debe escribir un programa para simular actividad para este problema de cruce de cañones:

- Simular cada mono como un proceso separado.
- Simular con un conjunto finito de monos, la dirección de cada uno se genera aleatoriamente. 
- Cada mono tarda en llegar entre 1 y 8 segundos. En conjunto, un montón de monos, lo que quieras, va a cruzar el cañón, con un generador de números aleatorios que especifican si están en movimiento hacia el este o en dirección oeste.
- Cada mono tarda en subir a la cuerda 1 segundo. 
- Todos los monos tardan lo mismo en cruzar la cuerda, 4 segundos

