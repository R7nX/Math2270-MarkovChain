import random
import numpy as np
from collections import Counter


graph_matrix = {
        "A" : (["B", "C"], [0.75, 0.5]),
        "B" : (["A", "C", "E"], [0.375, 0.25, 0.375]),
        "C" : (["A", "D", "E", "B"], [0.11 ,0.44, 0.22, 0.22]),
        "D" : (["C", "F"], [0.8, 0.2]),
        "F" : (["D", "E"], [0.5, 0.5]),
        "E" : (["B", "C", "F"], [0.5, 0.33, 0.167])
}


def get_next_state(current_state, matrix):
    options, probs = graph_matrix[current_state]

    # Unpack next states and their probabilities.
    return random.choices(options, weights=probs)[0]


def Markov_Chain(matrix, initial, steps):
    current = initial
    history = [current]
    for _ in range(steps):
        current = get_next_state(current, matrix)
        history.append(current)
    return history


def calc_steady_state(matrix, initial, steps):
    history = Markov_Chain(matrix, initial, steps)

    states_count = Counter(history)

    total_state = sum(states_count.values())

    steady_state_distribution = {}

    for state, count in states_count.items():
        steady_state_distribution[state] = count / total_state

    return steady_state_distribution



if __name__ == '__main__':
    print(calc_steady_state(graph_matrix, "A", 100000))

