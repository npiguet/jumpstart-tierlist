# Card-level analysis of all Jumpstart sets

## Methodology

The goal of this analysis is to figure out which cards are the most impactful in each Jumpstart pack. 

In order to do this, I had the Forge AI play another ~250k games against itself using jumpstart packs from the 3 main 
Jumpstart sets (JMP, J22 and J25) where each game used only boosters from the same set. I recorded each card that was 
played by the AI and saved that information together with the rest of the outcome of each game. 

I consider a card played if it either entered the battlefield or the stack, with some special rules to handle copies and 
players playing cards from the opponent's deck.

After collecting the results of all these games, I've aggregated them by pack and sorted all cards by total number 
of games won during which that card was played. This means that if the winner played 6 cards and the loser played 2, a 
win was counted for each of the 6 cards played by the winner, and a loss was counted for each of the 2 cards played by 
the loser.

I hesitated for a while between 3 sorting keys:
 - **By TrueSkill rating**: For some reason, the TrueSkill rating for this dataset was giving pretty nonsensical results
 - **By win rate**: That was better than TrueSkill rating, but it skewed heavily towards cards that were not played much 
   but almost guaranteed a win. To me this didn't sound correct, a card that wins 55% of the games it is played in and
   can be played every time it is drawn is better than a splashy card that wins 85% of the games it is played in, but 
   can only be played in 5% of the games because it costs too much.
 - **By number of wins**: Each pack participated in >2500 games, therefore each card has been drawn more or less the 
   same number of times. Recording how many times a card was played in a game that was won clearly records it impact on 
   the average game for that deck. Very strong cards that often can't be played are not as good as less spectacular but 
   more consistent cards.

## Results

The full list of results can be found (in CSV format) in the project's github repository.
 - [JMP](../ratings/random-withcards/JMP-cards.csv)
 - [J22](../ratings/random-withcards/J22-cards.csv)
 - [J25](../ratings/random-withcards/J25-cards.csv)

Here's a sample result of one pack of each set

### JMP Discarding (2)
*Pack win rate: 64%*

        Card Name            Mana Cost  Wins  Win Rate
    ==================================================
    1.  Ravenous Chupacabra        2BB   791       77%
    2.  Burglar Rat                 1B   684       68%
    3.  Liliana's Reaver           2BB   684       68%
    4.  Nyxathid                   1BB   660       67%
    5.  Phyrexian Rager             2B   655       68%
    6.  Wight of Precinct Six       1B   650       65%
    7.  Death's Approach             B   628       61%
    8.  Fell Specter                3B   605       70%
    9.  Slate Street Ruffian        2B   601       61%
    10. Entomber Exarch            2BB   586       66%
    11. Assassin's Strike          4BB   498       70%

It's interesting to see that [Assassin's Strike](https://scryfall.com/card/jmp/200/assassins-strike) despite carrying 
both card advantage and removal isn't all that impactful, probably because of its high cost. 

Instead, it's [Ravenous Chupacabra](https://scryfall.com/card/jmp/269/ravenous-chupacabra) that comes on top, with a 
2/2 body and removal packed in one card and carrying a reasonable cost that proves to have the highest positive impact.

[Liliana's Reaver](https://scryfall.com/card/jmp/251/lilianas-reaver) looks like a more powerful card to me but has a 
somewhat lower impact, probably because the opponent has an extra opportunity to remove it before it's powerful ability
has a chance to trigger. 

Overall, this looks like a pretty balanced pack, with 6 of 11 of cards sitting within 10% number of wins from 
each other.

### J22 Merfolk (2)
*Pack win rate: 65%*

        Card Name            Mana Cost  Wins  Win Rate
    ==================================================
    1.  Merrow Reejerey             2U   849       71%
    2.  One With the Wind           1U   804       73%
    3.  Crashing Tide               2U   779       62%
    4.  Svyelun of Sea and Sky     1UU   764       73%
    5.  Watertrap Weaver            2U   745       67%
    6.  Merfolk Pupil               1U   737       66%
    7.  Triton Shorestalker          U   733       69%
    8.  Synchronized Eviction       4U   710       69%
    9.  River Sneak                 1U   686       68%
    10. Windrider Patrol           3UU   654       70%
    11. Saltwater Stalwart          3U   619       66%
    12. Soul Read                   3U   454       60%

[Merrow Reejerey](https://scryfall.com/card/j22/61/merrow-reejerey) comes out as the best card in this pack. And it 
actually packs a significant punch a 2/2 body, an anthem effect and a tap/untap trigger. For just 3 mana it is a great 
deal.

[Soul Read](https://scryfall.com/card/j22/17/soul-read) on the other hand disappoints. It's modular nature is not really
good enough to save it. Nobody is going to hold 4 mana just to maybe counter one spell, and drawing 2 cards for 4 mana 
is also not great in blue. 

[Windrider Patrol](https://scryfall.com/card/bfz/89/windrider-patrol) has a decent win rate when it is played, but its 
cost and relatively less interesting abilities mean that there is often a better choice to spend 5 mana on. 

[One With the Wind](https://scryfall.com/card/j22/330/one-with-the-wind) shows that cheap, straightforward cards can 
have an outsized impact in this format.

### J25 Angels (1)
*Pack win rate: 60%*

        Card Name            Mana Cost  Wins  Win Rate
    ==================================================
    1.  Giada, Font of Hope         1W   780       67%
    2.  Serra Angel                3WW   712       71%
    3.  Pacifism                    1W   699       61%
    4.  Light of Hope                W   682       57%
    5.  Youthful Valkyrie           1W   680       64%
    6.  Inspiring Overseer          2W   664       68%
    7.  Celestial Enforcer          2W   619       62%
    8.  Secluded Steppe              -   619       57%
    9.  Faithful Pikemaster         3W   604       60%
    10. Herald of the Sun          4WW   585       65%
    11. Destroy Evil                1W   513       70%
    12. Starnheim Memento            3   472       52%

[Giada, Font of Hope](https://scryfall.com/card/fdn/141/giada-font-of-hope) is the powerhouse of this deck: Cheap, 
evasive, mana dork, anthem~ish all packed in one card. This one can be played every time it is drawn, and wins often
when played.

[Starnheim Memento](https://scryfall.com/card/j25/29/starnheim-memento) seems to be hard to place. It's hard to justify
playing a 3 mana rock when the deck has so many better options for that cost. It's win rate is so low it seems to drag 
the rest of the pack down. It helps cast big spells, but it's only useful if you draw big spells. Otherwise, by the time 
you can play it, it becomes a mostly dead card.  

[Destroy Evil](https://scryfall.com/card/j25/189/destroy-evil) is interesting because despite being so cheap it is not
played often. However, the ability to take out big juicy targets means that when it makes sense to play it, it pays off
well. It's restriction on creature power though means that it fails to make much of an impact in the end. 


### Cards with the most wins

With the methodology I used, comparing cards across packs is a bit sketchy. You end up with results that not only tell
you whether the card itself is good, but also if the rest of the pack supports that card properly.

- **JMP [Nature's way](https://scryfall.com/card/jmp/412/natures-way) (1G)**: A very good finisher. Can cause a lot of 
  damage in the right deck. Apparently JMP Tree-Hugging variants are the right deck.
- **J22 [Lyra Dawnbringer](https://scryfall.com/card/j22/210/lyra-dawnbringer) (3WW)**: It just makes you gain ALL the 
  life. If not removed this is both lethal for your opponent and heals you. Its cost is pretty high, but the other cards 
  in the deck probably make you gain enough life to keep you alive until you're able to draw and cast this card.  
- **J25 [Cynette, Jelly Drover](https://scryfall.com/card/j25/35/cynette-jelly-drover) (3U)**: I'm not entirely sure why
  this one ranks to the top of J25. It's not a bad card, but I just didn't think it was as good as the other 2. I assume 
  the extra body works well, and the anthem effect has good support in the rest of the pack.

### Cards with the highest win rate

This category illustrates why I chose to rank cards by total number of wins rather than win rate. It looks like cards 
with the highest win rate are all cards you'd play when you are already in a good situation. They won't get you out of 
trouble if you are behind, but they will help you win when you are in front. Therefore, they tend to not be played as 
often, and are kind of "win more" cards

- **JMP [Hungry Flames](https://scryfall.com/card/jmp/336/hungry-flames) (2R) 88%**: This card is played at less than 
  half the rate of the best cards. It gets rid of small creatures only and won't be able to remove the real threats.
  The extra 2 damage to face is a nothingburger. This is really only useful to remove the last chump blocker before you
  attack for the win. 
- **J22 [Feast of Blood](https://scryfall.com/card/j22/118/feast-of-blood) (1B) 90%**: Similar to Hungry Flames in its 
  role. It's played about 50% more often (though still not as much as better cards) probably because it is cheaper and 
  can remove any creature. The requirement of having 2 vampires on the board prevents this card back from being useful
  when you are in trouble.
- **J25 [Ghalta, Primal Hunger](https://scryfall.com/card/j25/77/ghalta-primal-hunger) (10GG) 81%**: Now this one is 
  a proper game winning card. It's pure raw dumb power. It's great if you can manage to cast it. Casting it is difficult 
  though, and that's why it lags behind in total number of wins despite it's high win rate.

## Interesting facts about the Forge AI

In my previous post, different people have pointed out the fact that Forge's AI was not a terribly good player. I have 
always admitted that it was true and was a source of error for all these analysis posts. I've never really been able to 
point out why exactly the AI was underperforming.

Now, the card data reveals an interesting thing: The AI just plain refuses to play some cards.

Minotaur Skullcleaver, Fling, Dualcaster Mage, Riddle of Lightning, Scrounging Bandar, Barter in Blood,
Jolrael, Mwonvuli Recluse, Ghoulcaller Gisa, Corpse Traders, Prismite, Seismic Elemental, Ormos, Archive Keeper, 
Chromatic Sphere, Terrarion, Capture Sphere, Serendib Efreet, etc...

None of these are actually played by the AI. Sometimes they are forced on the battlefield through some kind of 
reanimation effect, but for all of these cards, this happened less than 10 times in >2'700 games. Some of them never 
even touched the stack or the battlefield at all.

Some of these have downsides that the AI probably over-values, which could explain why they aren't played. But some are
pretty simple creatures who's only problem is being slightly worse the standard 1/1 for (1) cost curve. Some even have 
good abilities.

I don't think this fully invalidates the results I've had so far, but it clearly handicaps some packs that essentially 
have to play with one forever dead card.

That part is definitely an avenue for further research. 

In the meantime, feel free to dig through the [full CSV result files](../ratings/random-withcards) to see if you find
anything else that is interesing.   