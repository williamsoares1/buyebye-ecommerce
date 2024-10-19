CREATE FUNCTION notify_discount_expiration()
    RETURNS TRIGGER AS $$
    BEGIN
        IF NEW.end_date <= NOW() AND NEW.notified = FALSE THEN

            PERFORM pg_notify('expirated_discount', NEW.discount_id::TEXT);

            NEW.notified := TRUE;
        END IF;

        RETURN NEW;
    END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER discount_expiration_trigger
AFTER INSERT OR UPDATE ON discount
FOR EACH ROW
EXECUTE FUNCTION notify_discount_expiration();
