function createMap() {
  const S = CFG.SIZE;

  const map = Array.from({ length: S }, () =>
    Array.from({ length: S }, () => ({
      wumpus: false,
      wumpusAlive: false,
      pit: false,
      gold: false,
      bat: false,
    })),
  );

  const forbidden = new Set([key(S - 1, 0), key(S - 1, 1), key(S - 2, 0)]);

  function rndCell(extra = []) {
    const excl = new Set([...forbidden, ...extra]);
    let r, c;
    do {
      r = Math.floor(Math.random() * S);
      c = Math.floor(Math.random() * S);
    } while (excl.has(key(r, c)));
    return { r, c };
  }

  function place(prop, n) {
    for (let i = 0; i < n; i++) {
      const used = [];
      for (let r = 0; r < S; r++)
        for (let c = 0; c < S; c++) if (map[r][c][prop]) used.push(key(r, c));
      const { r, c } = rndCell(used);
      map[r][c][prop] = true;
      if (prop === "wumpus") map[r][c].wumpusAlive = true;
    }
  }

  place("wumpus", CFG.N_WUMPUS);
  place("pit", CFG.N_PITS);
  place("gold", CFG.N_GOLD);
  place("bat", CFG.N_BATS);

  return map;
}
