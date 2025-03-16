package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.*;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import seedu.address.logic.commands.AddTripCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Name;
import seedu.address.model.trip.Accommodation;
import seedu.address.model.trip.Itinerary;
import seedu.address.model.trip.Trip;
import seedu.address.model.trip.TripDate;
import seedu.address.model.trip.TripName;

/**
 * Parses input arguments and creates a new AddTripCommand object
 */
public class AddTripCommandParser implements Parser<AddTripCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the AddTripCommand
     * and returns an AddTripCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public AddTripCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_NAME, PREFIX_ACCOMMODATION, PREFIX_ITINERARY, PREFIX_CUSTOMER_NAME, PREFIX_DATE);

        if (!arePrefixesPresent(argMultimap, PREFIX_NAME, PREFIX_ACCOMMODATION, PREFIX_ITINERARY, PREFIX_CUSTOMER_NAME, PREFIX_DATE)
                || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddTripCommand.MESSAGE_USAGE));
        }

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_NAME, PREFIX_ACCOMMODATION, PREFIX_ITINERARY, PREFIX_DATE);
        TripName name = ParserUtil.parseTripName(argMultimap.getValue(PREFIX_NAME).get());
        Accommodation accommodation = ParserUtil.parseAccommodation(argMultimap.getValue(PREFIX_ACCOMMODATION).get());
        Itinerary itinerary = ParserUtil.parseItinerary(argMultimap.getValue(PREFIX_ITINERARY).get());
        TripDate date = ParserUtil.parseTripDate(argMultimap.getValue(PREFIX_DATE).get());

        List<String> customerNames = argMultimap.getAllValues(PREFIX_CUSTOMER_NAME);
        Set<Name> customerNameSet = new HashSet<>();
        for (String customerName : customerNames) {
            customerNameSet.add(ParserUtil.parseName(customerName));
        }

        Trip trip = new Trip(name, accommodation, itinerary, date, customerNameSet);
        return new AddTripCommand(trip);
    }

    /**
     * Returns true if none of the prefixes contains empty {@code Optional} values in the given
     * {@code ArgumentMultimap}.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }

}
