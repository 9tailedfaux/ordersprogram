IN=(
	"Breakfast 1,2,3"
	"Breakfast 2,3,1"
	"Breakfast 1,2,3,3,3"
	"Breakfast 1"
	"Lunch 1,2,3"
	"Lunch 1,2"
	"Lunch 1,1,2,3"
	"Lunch 1,2,2"
	"Lunch"
	"Dinner 1,2,3,4"
	"Dinner 1,2,3"
)
OUT=(
	"Breakfast: Eggs, Toast, Coffee"
	"Breakfast: Eggs, Toast, Coffee"
	"Breakfast: Eggs, Toast, Coffee(3)"
	"Side is required"
	"Lunch: Sandwich, Chips, Soda"
	"Lunch: Sandwich, Chips, Water"
	"You cannot have more than one Sandwich"
	"Lunch: Sandwich, Chips(2), Water"
	"Too few arguments. You must order something"
	"Dinner: Steak, Potatoes, Wine, Water, Cake"
	"Desert is required"
)
PASSED=0
FAILED=0
for i in ${!IN[@]}; do
	RESULT=$(./gradlew -q run --args="${IN[$i]}")
	echo "Input: ${IN[$i]}"
	echo "Expected output: ${OUT[$i]}"
	echo "Program ouput: ${RESULT}"
	if [ "${OUT[$i]}" = "${RESULT}" ]; then
		echo "PASSED"
		((PASSED++))
	else
		echo "FAILED"
		((FAILED++))
	fi
	echo
done

echo
echo "Passed: $PASSED"
echo "Failed: $FAILED"