export const currencies = ['INR', 'USD', 'EUR', 'GBP', 'AED', 'SGD'];

export function money(value, currency = 'INR') {
  return new Intl.NumberFormat('en-IN', { style: 'currency', currency, maximumFractionDigits: 2 }).format(Number(value || 0));
}

export function compactDate(value) {
  return new Intl.DateTimeFormat('en-IN', { day: '2-digit', month: 'short', hour: '2-digit', minute: '2-digit' }).format(new Date(value));
}
