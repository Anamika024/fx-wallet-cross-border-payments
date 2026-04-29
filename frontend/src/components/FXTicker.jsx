import { useFxTicker } from '../hooks/useFxTicker.js';

export default function FXTicker() {
  const rates = useFxTicker();
  return (
    <div className="flex flex-wrap gap-2 text-xs">
      {Object.entries(rates).filter(([code]) => code !== 'INR').map(([code, rate]) => (
        <span key={code} className="rounded-md border border-line bg-white px-2 py-1 font-medium">
          INR/{code} {Number(rate).toFixed(4)}
        </span>
      ))}
    </div>
  );
}
