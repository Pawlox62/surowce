const ctx = document.getElementById('priceChart').getContext('2d')
let chart = new Chart(ctx,{type:'line',data:{labels:[],datasets:[{label:'Cena',data:[],borderWidth:2}]},options:{responsive:true}})
let from=document.getElementById('dateFrom'),to=document.getElementById('dateTo')
let selectedId=null
function load(id){if(!id)return;selectedId=id;fetch(`/api/surowce/${id}/prices?from=${from.value}&to=${to.value}`).then(r=>r.json()).then(updateChart)}
function updateChart(data){chart.data.labels=data.map(p=>p.date);chart.data.datasets[0].data=data.map(p=>p.value);chart.update()}
document.querySelectorAll('tbody tr').forEach(tr=>tr.addEventListener('click',()=>load(tr.dataset.id)))
document.getElementById('filterBtn').addEventListener('click',()=>load(selectedId))
const sock=new SockJS('/ws');const stomp=Stomp.over(sock);stomp.connect({},()=>stomp.subscribe('/topic/refresh',()=>load(selectedId)))
