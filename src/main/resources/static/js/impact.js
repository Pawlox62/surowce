const ctx = document.getElementById('chart').getContext('2d');

let chart = new Chart(ctx, {
  type: 'line',
  data: {
    labels: [],
    datasets: [
      {
        label: 'Cena',
        data: [],
        borderWidth: 2,
        pointRadius: 3,
        borderColor: '#3b82f6',
        backgroundColor: '#3b82f6',
        order: 2
      },
      {
        type: 'scatter',
        label: 'Konflikty',
        data: [],
        pointRadius: 6,
        borderWidth: 2,
        borderColor: '#dc2626',
        backgroundColor: '#f87171',
        showLine: false,
        order: 1
      }
    ]
  },
  options: {
    responsive: true,
    plugins: {
      legend: { display: true },
      tooltip: {
        callbacks: {
          label: ctx => {
            if (ctx.datasetIndex === 1) {
              const d = ctx.raw;
              return [
                `Konflikt ID: ${d.conflictId}`,
                `Lokalizacja: ${d.location}`,
                `Strona A: ${d.sideA}`,
                `Strona B: ${d.sideB}`,
                `Data: ${d.x}`,
                `Cena: ${d.y}`
              ];
            }
            return `Cena: ${ctx.formattedValue}`;
          }
        }
      }
    },
    scales: {
      x: {
        type: 'time',
        time: {
          parser: 'yyyy-MM-dd',
          unit: 'month',
          displayFormats: {
            month: 'yyyy-MM'
          }
        },
        ticks: {
          maxRotation: 0,
          autoSkip: true
        }
      },
      y: {
        title: { display: true, text: 'Cena' }
      }
    },
    onClick: (evt, elements) => {
      if (elements.length && elements[0].datasetIndex === 1) {
        const idx = elements[0].index;
        const raw = chart.data.datasets[1].data[idx];
        const conflictId = raw.conflictId;
        window.location.href = `/konflikty/${conflictId}`;
      }
    }
  }
});

document.getElementById('btn').addEventListener('click', load);

function load() {
  const id   = document.getElementById('sel').value;
  const from = document.getElementById('from').value;
  const to   = document.getElementById('to').value;
  if (!id || !from || !to) return;

  Promise.all([
    fetch(`/api/surowce/${id}/prices?from=${from}&to=${to}`, { headers: auth() }).then(r => r.json()),
    fetch(`/api/konflikty/range?from=${from.slice(0,4)}&to=${to.slice(0,4)}`, { headers: auth() }).then(r => r.json())
  ]).then(([prices, conflicts]) => {
    if (!prices || prices.length === 0) {
      chart.data.datasets[0].data = [];
      chart.data.datasets[1].data = [];
      chart.update();
      return;
    }
}
    const pricePoints = prices.map(p => ({
      x: p.date,
      y: Number(p.value)
    }));
    chart.data.datasets[0].data = pricePoints;

    const datePriceMap = new Map();
    prices.forEach(p => datePriceMap.set(p.date, Number(p.value)));
    const sortedDates = prices
      .map(p => new Date(p.date + 'T00:00:00'))
      .sort((a, b) => a - b);

    function findClosestPrice(targetDateStr) {
      if (datePriceMap.has(targetDateStr)) {
        return datePriceMap.get(targetDateStr);
      }
      const targetD = new Date(targetDateStr + 'T00:00:00');
      let candidate = null;
      for (let d of sortedDates) {
        if (d.getTime() <= targetD.getTime()) {
          candidate = d;
        } else {
          break;
        }
      }
      if (candidate) {
        const iso = candidate.toISOString().slice(0, 10);
        return datePriceMap.get(iso);
      }
      return null;
    }

    const usedYears = new Set();
    const conflictPoints = [];
    conflicts.forEach(c => {
      const year = c.rok;
      if (!usedYears.has(year)) {
        usedYears.add(year);
        const conflictDate = `${year}-01-01`;
        const priceAtConflict = findClosestPrice(conflictDate);
        if (priceAtConflict !== null) {
          conflictPoints.push({
            x: conflictDate,
            y: priceAtConflict,
            conflictId: c.id,
            location: c.location,
            sideA: c.sideA || '—',
            sideB: c.sideB || '—'
          });
        }
      }
    });
    chart.data.datasets[1].data = conflictPoints;

    chart.update();
  });
}

function auth() {
  return { 'Authorization': 'Basic ' + btoa('user:haslo') };
}

const socket = new SockJS('/ws');
const stomp  = Stomp.over(socket);
stomp.connect({}, () => {
  stomp.subscribe('/topic/price-update', () => {
    location.reload();
  });
});
