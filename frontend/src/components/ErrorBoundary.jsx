import React from 'react';

export default class ErrorBoundary extends React.Component {
  constructor(props) {
    super(props);
    this.state = { error: null };
  }

  static getDerivedStateFromError(error) {
    return { error };
  }

  render() {
    if (this.state.error) {
      return (
        <main className="grid min-h-screen place-items-center bg-mist px-4 text-ink">
          <section className="w-full max-w-md rounded-md border border-line bg-white p-6">
            <p className="text-xs font-semibold uppercase text-coral">App error</p>
            <h1 className="mt-1 text-2xl font-semibold">Reload the FX Wallet app</h1>
            <p className="mt-3 text-sm text-slate-600">{this.state.error.message}</p>
            <button className="btn-primary mt-5 w-full" onClick={() => window.location.assign('/login')}>
              Back to login
            </button>
          </section>
        </main>
      );
    }
    return this.props.children;
  }
}
