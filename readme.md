<h2>TogglePvP ⚔</h2>

Use Issues section to report bugs or submit suggestions.

<h3>Commands</h3>
Permission: <code>togglepvp.command</code> (given by default to everyone) :
<ul>
<li>/pvp help- displays help message</li>
</ul>

Permission: <code>togglepvp.command.toggle</code> (given by default to everyone) :
<ul>
<li>/pvp toggle - toggles PvP</li>
<li>/pvp on - enables PvP (aliases: enable)</li>
<li>/pvp off - disables PvP (aliases: disable)</li>
</ul>

Permission: <code>togglepvp.command.toggle.others</code> (given by default to ops) :
<ul>
<li>/pvp toggle [player] - toggles other player's PvP</li>
<li>/pvp on [player] - enables other player's PvP (aliases: enable)</li>
<li>/pvp off [player] - disables other player's PvP (aliases: disable)</li>
</ul>

<h3>Features</h3>
Player who disables PvP cannot get hurt by any player induced damage.
They also can't deal any damage to other players. 
This includes basic attacks, projectiles, splash potions,
lingering potions and lightning strikes from channeling trident enchant.
Additionally lightning strikes from channeling cannot spawn fire under
players, who have pvp disabled and it's impossible to use
fishing rod on players who have pvp disabled.

There is also a feature that disallows pouring lava or using
flint and steel nearby players with disabled pvp. You can
either disable it in config or set its radius.

All the protections are extended to tameable entities (pets) that set player's uuid as their owner on tame.

Pets will not attack players with pvp off or if their owner has pvp off.



