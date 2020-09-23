# Fantasy Football Value Drafting
- Take the decision out of your fantasy football draft by getting suggestions of who to draft next  
- Enter draft picks as they occur live then run a simulation on your pick to get player values at each position  
- Import your league's default rankings, your personal rankings, and league settings beforehand  

The interface and internals are all currently in Excel document that can run macros. Eventually, there will be migration to code, but Excel is the means for now.

## User Interface

### League Settings
- Roster size at each position
- Number and name of each team
- User draft position
- Point values of different events (ex. pass yards, pass TD, etc.)

### Personal Rankings
- Enter names of players in a list for each position

### ESPN Import
- Currently a way to import default league rankings
- Will need significant upgrade to use other sites and update automatically

### Live Draft Picks
- Enter ID of players as they are picked in each round. IDs (OVR) can be found in Default Rankings or Live Draft Suggestions VOR/VO2

### Live Draft Suggestions VOR
- Shows value over replacement (VOR) for every player, which are expected season points above position's baseline players in fantasy league
- Values are adjusted for expected start percentage on fantasy teams
- Expected points use recent historical position data found in FFYearlyStatDatabase, weighted for more recent years. A function exists for expected points based on ranking at position.
- Useful for a draft where you think others aren't following the default rankings. Getting the players most valuable beyond a waiver pick-up is the best way to get the team you want when you know little about others' intentions.

### Live Draft Suggestions VO2
- Similar to VOR, but value over expected player available in 2 rounds
- Must run simulation under MODELRISK with samples = # picks before 2 rounds in future to fill in values
- Suggested method of choosing who to draft next. This incorporates where the market is, whether you are high on players who will be available, and other factors into one number

### Fantasy Teams
- Teams shown based on entries in Live Draft Picks.
- Must be filled in by running Java program in same folder.

## Internals

### Default Rankings Import 20xx
- Imports all players from FantasyPros in top 400
- Each year, standard deviation must be recalculated for each position. It is used to generate a function correlating overall ranking to ranking deviation.

### Default Rankings
- Takes Default Rankings Import into consistent format by position
- Has standard deviation column taken from Default Rankings Import 20xx

### Player Availability
- Helper used in Monte Carlo simulation for projecting who is picked 2 rounds in advance.
- Players are given random value from their distribution each pick through, and the top unpicked player is assumed to be the choice for the current pick.
- By doing this Monte Carlo simulation for every pick for 2 rounds a number of times, an expected top player available for each round can be found.
- Beyond 2 rounds, past players rarely affect who will be chosen since the market should have truly corrected by then. Using player default ranking is just as predictive as Monte Carlo pick prediction

## Future Upgrades
- Migrate to programming language so user doesn't have access to internals. Right now, everything is in 1 Excel sheet. A front-end GUI and back-end program is the ideal way.
- Enter if users are autodrafting. This will make guessing their pick significantly easier since it will pick the top-ranked player at a position of need.
- Make player suggestions more accurate by looking down the road positionally. Maybe it always makes sense to draft a RB in round 6 given your and default rankings. Knowing that in round 3 would be useful.
- Use variance between beginning of season rankings and actual finish in combination with historical data to make a more accurate expected value. For instance, the top defense is notoriously hard to predict, so the expected value of your top-ranked defense should be lower to reflect that.
- Allow user to weight VO2 and VOR into one combined number based on how they think their league will draft.
