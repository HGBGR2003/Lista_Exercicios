function updatePerceptions() {
  const { row: r, col: c } = G.agent;
  const adj = getAdj(r, c);

  G.percs.stench = adj.some(
    ([ar, ac]) => G.map[ar][ac].wumpus && G.map[ar][ac].wumpusAlive,
  );
  G.percs.breeze = adj.some(([ar, ac]) => G.map[ar][ac].pit);
  G.percs.rustle = adj.some(([ar, ac]) => G.map[ar][ac].bat);
  G.percs.glitter = G.map[r][c].gold === true;
}

function getAdj(r, c) {
  const S = CFG.SIZE,
    res = [];
  if (r > 0) res.push([r - 1, c]);
  if (r < S - 1) res.push([r + 1, c]);
  if (c > 0) res.push([r, c - 1]);
  if (c < S - 1) res.push([r, c + 1]);
  return res;
}
