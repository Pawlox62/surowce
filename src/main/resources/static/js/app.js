const ctx = document.getElementById('priceChart').getContext('2d');

let chart = new Chart(ctx, {
  type: 'line',
  data: {
    labels: [],
    datasets: [
      {
        label: 'Cena',
        data: [],
        borderWidth: 2
      }
    ]
  },
  options: {
    responsive: true,
    scales: {
      x: {
        type: 'time',
        time: {
          unit: 'day'
        }
      },
      y: {
        beginAtZero: false
      }
    }
  }
});

let from = document.getElementById('dateFrom'),
    to   = document.getElementById('dateTo');
let selectedId = null;

function load(id) {
  if (!id) return;
  selectedId = id;

  const fromDate = from.value || '1900-01-01';
  const toDate   = to.value   || '2025-12-31';

  fetch(`/api/surowce/${id}/prices?from=${fromDate}&to=${toDate}`)
    .then(response => {
      if (!response.ok) {
        throw new Error(`Błąd serwera: ${response.status}`);
      }
      return response.json();
    })
    .then(updateChart)
    .catch(err => {
      console.error('Błąd podczas pobierania danych:', err);
    });
}

function updateChart(data) {
  chart.data.labels = data.map(p => p.date);
  chart.data.datasets[0].data = data.map(p => p.value);
  chart.update();
}

document.querySelectorAll('tbody tr').forEach(tr => {
  tr.addEventListener('click', () => {
    load(tr.dataset.id);

    document.querySelectorAll('tbody tr').forEach(r => r.classList.remove('bg-gray-200'));
    tr.classList.add('bg-gray-200');
  });
});

document.getElementById('filterBtn').addEventListener('click', () => {
  load(selectedId);
});

const sock = new SockJS('/ws');
const stomp = Stomp.over(sock);
stomp.connect({}, () => {
  stomp.subscribe('/topic/refresh', () => {
    load(selectedId);
  });
});
